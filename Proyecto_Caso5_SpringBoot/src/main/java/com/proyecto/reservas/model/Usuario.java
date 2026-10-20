package com.proyecto.reservas.model;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Column(unique = true, nullable = false)
    private String correo;

    @Enumerated(EnumType.STRING)
    private RolUsuario rol;

    public Usuario() {}

    public Usuario(String nombre, String correo, RolUsuario rol) {
        this.nombre = nombre;
        this.correo = correo;
        this.rol = rol;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getCorreo() { return correo; }
    public RolUsuario getRol() { return rol; }

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setCorreo(String correo) { this.correo = correo; }
    public void setRol(RolUsuario rol) { this.rol = rol; }
    public void setId(Long id) { this.id = id; }
}
