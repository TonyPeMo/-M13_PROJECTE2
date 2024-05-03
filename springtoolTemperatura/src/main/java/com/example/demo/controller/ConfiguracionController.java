package com.example.demo.controller;

import com.example.demo.bean.Configuracion;
import com.example.demo.bean.Usuario;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.ConfiguracionRepository;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/configuracion")
public class ConfiguracionController {

    private final ConfiguracionRepository configuracionRepository;

    private final UsuarioRepository usuarioRepository;

    public ConfiguracionController(ConfiguracionRepository configuracionRepository, UsuarioRepository usuarioRepository) {
        this.configuracionRepository = configuracionRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // Obtener todas las configuraciones
    // no se va a usar en teoria
    @GetMapping
    public List<Configuracion> getAllConfiguraciones() {
        return configuracionRepository.findAll();
    }

    // Crear una nueva configuración
    // no se va a usar en teoria
    @PostMapping
    public Configuracion createConfiguracion(@RequestBody Configuracion configuracion) {
        return configuracionRepository.save(configuracion);
    }

    // Obtener la configuración de un usuario
    @GetMapping("/{idUsuario}")
    public Configuracion getConfiguracion(@PathVariable Integer idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", idUsuario));
        return usuario.getConfiguracion();
    }

    // Actualizar la configuración de un usuario
    @PutMapping("/{idUsuario}")
    public Configuracion updateConfiguracion(@PathVariable Integer idUsuario, @RequestBody Configuracion configuracionDetails) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", idUsuario));
        Configuracion configuracion = usuario.getConfiguracion();

        // Aquí puedes actualizar los campos de la configuración
        configuracion.setColorFrio(configuracionDetails.getColorFrio());
        configuracion.setColorOptimo(configuracionDetails.getColorOptimo());
        configuracion.setColorCalor(configuracionDetails.getColorCalor());
        configuracion.setNotFrio(configuracionDetails.getNotFrio());
        configuracion.setNotCalor(configuracionDetails.getNotCalor());
        configuracion.settFrio(configuracionDetails.gettFrio());
        configuracion.settOptimaMin(configuracionDetails.gettOptimaMin());
        configuracion.settOptimaMax(configuracionDetails.gettOptimaMax());
        configuracion.settCalor(configuracionDetails.gettCalor());

        return configuracionRepository.save(configuracion);
    }

    // Eliminar una configuración
    // no se va a usar en teoria
    @DeleteMapping("/{id}")
    public void deleteConfiguracion(@PathVariable Integer id) {
        Configuracion configuracion = configuracionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Configuracion", "id", id));
        configuracionRepository.delete(configuracion);
    }
}