package com.proyecto.reservas.service;

import com.proyecto.reservas.exception.ConflictoReservaException;
import com.proyecto.reservas.model.*;
import com.proyecto.reservas.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservaServiceTest {

    @Mock private ReservaRepository reservaRepository;
    @Mock private EspacioRepository espacioRepository;
    @Mock private UsuarioRepository usuarioRepository;

    private ReservaService reservaService;
    private Espacio espacioOk;
    private Usuario usuarioOk;

    @BeforeEach
    void setUp() {
        reservaService = new ReservaService(reservaRepository, espacioRepository, usuarioRepository);
        espacioOk = mock(Espacio.class);
        lenient().when(espacioOk.getId()).thenReturn(1L);
        lenient().when(espacioOk.validarCapacidad(anyInt())).thenReturn(true);
        usuarioOk = mock(Usuario.class);
    }

    private void setId(Reserva r, Long id) {
        try {
            Field f = Reserva.class.getDeclaredField("id");
            f.setAccessible(true);
            f.set(r, id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ReservaService.CrearReservaRequest reqValido() {
        ReservaService.CrearReservaRequest r = new ReservaService.CrearReservaRequest();
        r.espacioId = 1L;
        r.usuarioId = 1L;
        r.fecha = LocalDate.now().plusDays(1);
        r.horaInicio = LocalTime.of(8,0);
        r.horaFin = LocalTime.of(10,0);
        r.cantidadAsistentes = 10;
        r.motivo = "Reunión";
        return r;
    }

    @Test
    void test1_crearOk() {
        var req = reqValido();
        when(espacioRepository.findById(1L)).thenReturn(Optional.of(espacioOk));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioOk));
        when(reservaRepository.findByEspacioId(1L)).thenReturn(List.of());
        when(reservaRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        assertNotNull(reservaService.crear(req));
    }

    @Test
    void test2_fechaInvalida() {
        var req = reqValido();
        req.horaFin = LocalTime.of(7,0);
        assertThrows(IllegalArgumentException.class, () -> reservaService.crear(req));
    }

    @Test
    void test3_capacidad() {
        var req = reqValido();
        req.cantidadAsistentes = 100;
        when(espacioRepository.findById(1L)).thenReturn(Optional.of(espacioOk));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioOk));
        when(espacioOk.validarCapacidad(100)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> reservaService.crear(req));
    }

    @Test
    void test4_cruce() {
        var req = reqValido();
        when(espacioRepository.findById(1L)).thenReturn(Optional.of(espacioOk));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioOk));
        Reserva existente = new Reserva(espacioOk, usuarioOk, req.fecha, LocalTime.of(9,0), LocalTime.of(11,0), 5, "Otra");
        existente.setEstado(EstadoReserva.APROBADA);
        when(reservaRepository.findByEspacioId(1L)).thenReturn(List.of(existente));
        assertThrows(ConflictoReservaException.class, () -> reservaService.crear(req));
    }

    @Test
    void test5_motivoBlank() {
        var req = reqValido();
        req.motivo = "   ";
        assertThrows(IllegalArgumentException.class, () -> reservaService.crear(req));
    }

    @Test
    void test6_limiteExactoBordeNoCruce() {
        var req = reqValido();
        req.horaInicio = LocalTime.of(10,0);
        req.horaFin = LocalTime.of(12,0);
        when(espacioRepository.findById(1L)).thenReturn(Optional.of(espacioOk));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioOk));
        Reserva existente = new Reserva(espacioOk, usuarioOk, req.fecha, LocalTime.of(8,0), LocalTime.of(10,0), 5, "Otra");
        existente.setEstado(EstadoReserva.APROBADA);
        when(reservaRepository.findByEspacioId(1L)).thenReturn(List.of(existente));
        when(reservaRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        assertDoesNotThrow(() -> reservaService.crear(req));
    }

    @Test
    void test7_aprobarOk() {
        Reserva r = new Reserva(espacioOk, usuarioOk, LocalDate.now().plusDays(1), LocalTime.of(8,0), LocalTime.of(10,0), 10, "Test");
        setId(r, 1L);
        r.setEstado(EstadoReserva.PENDIENTE);
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(r));
        when(reservaRepository.findByEspacioId(1L)).thenReturn(List.of());
        when(reservaRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        assertEquals(EstadoReserva.APROBADA, reservaService.aprobar(1L).getEstado());
    }

    @Test
    void test8_aprobarCruce() {
        Reserva r = new Reserva(espacioOk, usuarioOk, LocalDate.now().plusDays(1), LocalTime.of(8,0), LocalTime.of(10,0), 10, "Test");
        setId(r, 1L);
        r.setEstado(EstadoReserva.PENDIENTE);
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(r));
        Reserva existente = new Reserva(espacioOk, usuarioOk, r.getFecha(), LocalTime.of(9,0), LocalTime.of(11,0), 5, "Otra");
        setId(existente, 2L);
        existente.setEstado(EstadoReserva.APROBADA);
        when(reservaRepository.findByEspacioId(1L)).thenReturn(List.of(existente));
        assertThrows(ConflictoReservaException.class, () -> reservaService.aprobar(1L));
    }

    @Test
    void test9_aprobarNoPendiente() {
        Reserva r = new Reserva(espacioOk, usuarioOk, LocalDate.now().plusDays(1), LocalTime.of(8,0), LocalTime.of(10,0), 10, "Test");
        setId(r, 1L);
        r.setEstado(EstadoReserva.APROBADA);
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(r));
        assertThrows(IllegalArgumentException.class, () -> reservaService.aprobar(1L));
    }
}