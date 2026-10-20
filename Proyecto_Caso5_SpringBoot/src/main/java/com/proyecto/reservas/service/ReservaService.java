package com.proyecto.reservas.service;

import com.proyecto.reservas.exception.ConflictoReservaException;
import com.proyecto.reservas.exception.RecursoNoEncontradoException;
import com.proyecto.reservas.model.*;
import com.proyecto.reservas.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class ReservaService {
    private final ReservaRepository reservaRepository;
    private final EspacioRepository espacioRepository;
    private final UsuarioRepository usuarioRepository;

    public ReservaService(ReservaRepository reservaRepository,
                          EspacioRepository espacioRepository,
                          UsuarioRepository usuarioRepository) {
        this.reservaRepository = reservaRepository;
        this.espacioRepository = espacioRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public static class CrearReservaRequest {
        public Long espacioId;
        public Long usuarioId;
        public LocalDate fecha;
        public LocalTime horaInicio;
        public LocalTime horaFin;
        public int cantidadAsistentes;
        public String motivo;
    }

    @Transactional
    public Reserva crear(CrearReservaRequest datos) {
        if (datos == null || datos.espacioId == null || datos.usuarioId == null) {
            throw new IllegalArgumentException("Debe indicar espacio y responsable.");
        }
        if (datos.fecha == null || datos.horaInicio == null || datos.horaFin == null) {
            throw new IllegalArgumentException("La fecha y las horas son obligatorias.");
        }
        if (!datos.horaFin.isAfter(datos.horaInicio)) {
            throw new IllegalArgumentException("La hora final debe ser posterior a la inicial.");
        }
        if (datos.cantidadAsistentes <= 0) {
            throw new IllegalArgumentException("La cantidad de asistentes debe ser mayor que cero.");
        }
        if (datos.motivo == null || datos.motivo.isBlank()) {
            throw new IllegalArgumentException("El motivo de la reserva es obligatorio.");
        }

        Espacio espacio = espacioRepository.findById(datos.espacioId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Espacio no encontrado."));
        Usuario usuario = usuarioRepository.findById(datos.usuarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario responsable no encontrado."));

        if (!espacio.validarCapacidad(datos.cantidadAsistentes)) {
            throw new IllegalArgumentException(
                    "El espacio está en mantenimiento o no cumple con la capacidad/equipos requeridos.");
        }

        Reserva nueva = new Reserva(espacio, usuario, datos.fecha,
                datos.horaInicio, datos.horaFin, datos.cantidadAsistentes, datos.motivo.trim());

        validarCruce(nueva, false);
        return reservaRepository.save(nueva);
    }

    public List<Reserva> listar() {
        return reservaRepository.findAll();
    }

    public Reserva buscar(Long id) {
        return reservaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Reserva no encontrada: " + id));
    }

    @Transactional
    public Reserva aprobar(Long id) {
        Reserva reserva = buscar(id);
        if (reserva.getEstado() != EstadoReserva.PENDIENTE) {
            throw new IllegalArgumentException("Solo se pueden aprobar reservas pendientes.");
        }
        if (!reserva.getEspacio().validarCapacidad(reserva.getCantidadAsistentes())) {
            throw new IllegalArgumentException("El espacio ya no está disponible o no cumple la capacidad.");
        }
        validarCruce(reserva, true);
        reserva.setEstado(EstadoReserva.APROBADA);
        return reservaRepository.save(reserva);
    }

    public Reserva rechazar(Long id, String motivo) {
        Reserva reserva = buscar(id);
        if (reserva.getEstado() != EstadoReserva.PENDIENTE) {
            throw new IllegalArgumentException("Solo se pueden rechazar reservas pendientes.");
        }
        if (motivo == null || motivo.isBlank()) {
            throw new IllegalArgumentException("Debe indicar el motivo del rechazo.");
        }
        reserva.setEstado(EstadoReserva.RECHAZADA);
        reserva.setMotivoDecision(motivo.trim());
        return reservaRepository.save(reserva);
    }

    public Reserva cancelar(Long id, String motivo) {
        Reserva reserva = buscar(id);
        if (reserva.getEstado() != EstadoReserva.PENDIENTE
                && reserva.getEstado() != EstadoReserva.APROBADA) {
            throw new IllegalArgumentException("Solo se pueden cancelar reservas pendientes o aprobadas.");
        }
        if (motivo == null || motivo.isBlank()) {
            throw new IllegalArgumentException("Debe indicar el motivo de cancelación.");
        }
        reserva.setEstado(EstadoReserva.CANCELADA);
        reserva.setMotivoDecision(motivo.trim());
        return reservaRepository.save(reserva);
    }

    private void validarCruce(Reserva nueva, boolean esAprobacion) {
        List<Reserva> delEspacio = reservaRepository.findByEspacioId(nueva.getEspacio().getId());
        for (Reserva existente : delEspacio) {
            if (nueva.getId() != null && nueva.getId().equals(existente.getId())) {
                continue;
            }
            boolean activa = existente.getEstado() == EstadoReserva.PENDIENTE
                    || existente.getEstado() == EstadoReserva.APROBADA;
            if (activa && nueva.seCruzaCon(existente)) {
                throw new ConflictoReservaException(
                        "Ya existe una reserva pendiente o aprobada que se cruza con ese horario.");
            }
        }
    }
}
