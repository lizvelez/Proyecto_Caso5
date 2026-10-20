package com.proyecto.reservas.model;

import jakarta.persistence.*;

@Entity
public class Espacio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Enumerated(EnumType.STRING)
    private TipoEspacio tipo;

    private int capacidad;
    private Integer cantidadEquipos;
    private boolean sonidoProfesional;
    private boolean enMantenimiento;

    public Espacio() {}

    public Espacio(String nombre, TipoEspacio tipo, int capacidad,
                   Integer cantidadEquipos, boolean sonidoProfesional,
                   boolean enMantenimiento) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.capacidad = capacidad;
        this.cantidadEquipos = cantidadEquipos;
        this.sonidoProfesional = sonidoProfesional;
        this.enMantenimiento = enMantenimiento;
    }

    public boolean validarCapacidad(int asistentes) {
        if (enMantenimiento || asistentes <= 0 || asistentes > capacidad) {
            return false;
        }
        if (tipo == TipoEspacio.LABORATORIO) {
            return cantidadEquipos != null && asistentes <= cantidadEquipos;
        }
        return true;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public TipoEspacio getTipo() { return tipo; }
    public int getCapacidad() { return capacidad; }
    public Integer getCantidadEquipos() { return cantidadEquipos; }
    public boolean isSonidoProfesional() { return sonidoProfesional; }
    public boolean isEnMantenimiento() { return enMantenimiento; }

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setTipo(TipoEspacio tipo) { this.tipo = tipo; }
    public void setCapacidad(int capacidad) { this.capacidad = capacidad; }
    public void setCantidadEquipos(Integer cantidadEquipos) { this.cantidadEquipos = cantidadEquipos; }
    public void setSonidoProfesional(boolean sonidoProfesional) { this.sonidoProfesional = sonidoProfesional; }
    public void setEnMantenimiento(boolean enMantenimiento) { this.enMantenimiento = enMantenimiento; }
    public void setId(Long id) { this.id = id; }
}
