package com.example.demo.repository;

import com.example.demo.bean.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Usuario findByUsername(String username);
    List<Usuario> getAllUsuarios();
    Usuario createUsuario(Usuario usuario);
    Usuario updateUsuario(Integer id, Usuario usuario);
    void deleteUsuarioBy(Integer id);
}