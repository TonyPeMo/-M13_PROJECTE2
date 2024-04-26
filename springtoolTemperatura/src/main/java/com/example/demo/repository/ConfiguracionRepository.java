package com.example.demo.repository;

import com.example.demo.bean.Configuracion;
import com.example.demo.bean.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfiguracionRepository extends JpaRepository<Configuracion, Integer> {
    Configuracion findByUsuario(Usuario usuario);

}
