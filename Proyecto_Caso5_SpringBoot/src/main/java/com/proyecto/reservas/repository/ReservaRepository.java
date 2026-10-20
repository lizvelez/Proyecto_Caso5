package com.proyecto.reservas.repository;

import com.proyecto.reservas.model.Reserva;
import com.proyecto.reservas.model.EstadoReserva;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findByEspacioId(Long espacioId);
    List<Reserva> findByResponsableId(Long responsableId);
    List<Reserva> findByEstado(EstadoReserva estado);
}
