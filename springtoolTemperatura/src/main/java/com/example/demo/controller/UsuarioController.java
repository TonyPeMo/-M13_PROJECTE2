package com.example.demo.controller;

import com.example.demo.bean.Configuracion;
import com.example.demo.bean.Usuario;
import com.example.demo.repository.ConfiguracionRepository;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioService;

    @Autowired
    private ConfiguracionRepository configuracionRepository;

    @GetMapping
    public List<Usuario> getAllUsuarios() {
        return usuarioService.getAllUsuarios();
    }

    @PostMapping
    public Usuario createUsuario(@RequestBody Usuario usuario) {
        // Guarda el usuario en la base de datos
        Usuario savedUsuario = usuarioService.save(usuario);

        // Crea una nueva configuración y la asocia con el usuario
        Configuracion configuracion = new Configuracion();
        configuracion.setUsuario(savedUsuario);
        configuracionRepository.save(configuracion);

        // Devuelve el usuario guardado
        return savedUsuario;
    }

    @PutMapping("/{id}")
    public Usuario updateUsuario(@PathVariable Integer id, @RequestBody Usuario usuario) {
        return usuarioService.updateUsuario(id, usuario);
    }

    @DeleteMapping("/{id}")
    public void deleteUsuario(@PathVariable Integer id) {
        usuarioService.deleteUsuarioBy(id);
    }
}