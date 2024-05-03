package com.example.demo.repository;

import com.example.demo.bean.Usuario;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    void deleteUsuarioByIdUser(Integer id);
    @Transactional
    void deleteById(Integer id);

}