package com.proyecto.reservas.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Espacio espacio;

    @ManyToOne(optional = false)
    private Usuario responsable;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false)
    private LocalTime horaInicio;

    @Column(nullable = false)
    private LocalTime horaFin;

    private int cantidadAsistentes;

    @Column(nullable = false, length = 500)
    private String motivo;

    @Enumerated(EnumType.STRING)
    private EstadoReserva estado = EstadoReserva.PENDIENTE;

    private String motivoDecision;

    public Reserva() {}

    public Reserva(Espacio espacio, Usuario responsable, LocalDate fecha,
                   LocalTime horaInicio, LocalTime horaFin,
                   int cantidadAsistentes, String motivo) {
        this.espacio = espacio;
        this.responsable = responsable;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.cantidadAsistentes = cantidadAsistentes;
        this.motivo = motivo;
        this.estado = EstadoReserva.PENDIENTE;
    }

    public boolean seCruzaCon(Reserva otra) {
        return fecha.equals(otra.fecha)
                && horaInicio.isBefore(otra.horaFin)
                && otra.horaInicio.isBefore(horaFin);
    }

    public Long getId() { return id; }
    public Espacio getEspacio() { return espacio; }
    public Usuario getResponsable() { return responsable; }
    public LocalDate getFecha() { return fecha; }
    public LocalTime getHoraInicio() { return horaInicio; }
    public LocalTime getHoraFin() { return horaFin; }
    public int getCantidadAsistentes() { return cantidadAsistentes; }
    public String getMotivo() { return motivo; }
    public EstadoReserva getEstado() { return estado; }
    public String getMotivoDecision() { return motivoDecision; }

    public void setEstado(EstadoReserva estado) { this.estado = estado; }
    public void setMotivoDecision(String motivoDecision) { this.motivoDecision = motivoDecision; }
    public void setEspacio(Espacio espacio) { this.espacio = espacio; }
    public void setResponsable(Usuario responsable) { this.responsable = responsable; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public void setHoraInicio(LocalTime horaInicio) { this.horaInicio = horaInicio; }
    public void setHoraFin(LocalTime horaFin) { this.horaFin = horaFin; }
    public void setCantidadAsistentes(int cantidadAsistentes) { this.cantidadAsistentes = cantidadAsistentes; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
}
