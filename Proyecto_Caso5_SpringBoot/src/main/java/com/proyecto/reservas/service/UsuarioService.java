package com.proyecto.reservas.service;

import com.proyecto.reservas.exception.RecursoNoEncontradoException;
import com.proyecto.reservas.model.Usuario;
import com.proyecto.reservas.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UsuarioService {
    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    public Usuario crear(Usuario usuario) {
        validar(usuario);
        if (repository.existsByCorreo(usuario.getCorreo())) {
            throw new IllegalArgumentException("Ya existe un usuario con ese correo.");
        }
        return repository.save(usuario);
    }

    public List<Usuario> listar() {
        return repository.findAll();
    }

    public Usuario buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado: " + id));
    }

    private void validar(Usuario u) {
        if (u.getNombre() == null || u.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del usuario es obligatorio.");
        }
        if (u.getCorreo() == null || !u.getCorreo().contains("@")) {
            throw new IllegalArgumentException("Ingrese un correo válido.");
        }
        if (u.getRol() == null) {
            throw new IllegalArgumentException("El rol es obligatorio.");
        }
    }
}
