package com.example.demo.controller;

import com.example.demo.bean.Aulas;
import com.example.demo.bean.Registros;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.AulasRepository;
import com.example.demo.repository.RegistrosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/registros")
public class RegistrosController {

    private final RegistrosRepository registrosRepository;

    private final AulasRepository aulasRepository;

    @Autowired
    public RegistrosController(RegistrosRepository registrosRepository, AulasRepository aulasRepository) {
        this.registrosRepository = registrosRepository;
        this.aulasRepository = aulasRepository;
    }

    // Obtener todos los registros
    // no se va a usar en teoria
    @GetMapping
    public List<Registros> getAllRegistros() {
        return registrosRepository.findAll();
    }

    // Obtener los registros de un aula
    @GetMapping("/aula/{idAula}")
    public List<Registros> getRegistrosByAula(@PathVariable Integer idAula) {
        Aulas aula = aulasRepository.findById(idAula)
                .orElseThrow(() -> new ResourceNotFoundException("Aula", "id", idAula));
        return registrosRepository.findByAulas(aula);
    }

    // Obtener los registros de una aula por nombre
    @GetMapping("/aula/{nombreAula}")
    public List<Registros> getRegistrosByAula(@PathVariable String nombreAula) {
        Aulas aula = aulasRepository.findByNomAula(nombreAula);
        return registrosRepository.findByAulas(aula);
    }

    // Obtener todos los registros entre dos fechas
    // no se va a usar en teoria
    @GetMapping("/fecha")
    public List<Registros> getRegistrosByFecha(@RequestParam Date fechaInicio, @RequestParam Date fechaFin) {
        return registrosRepository.findByFechaBetween(fechaInicio, fechaFin);
    }

    //te devuelve todos los registros de una aula entre dos fechas por ID
    @GetMapping("/aula/{idAula}/registros")
    public List<Registros> getRegistrosByAulaAndFecha(@PathVariable Integer idAula, @RequestParam Date fechaInicio, @RequestParam Date fechaFin) {
        Aulas aula = aulasRepository.findById(idAula).orElseThrow(() -> new ResourceNotFoundException("Aula", "id", idAula));
        return registrosRepository.findByAulasAndFechaBetween(aula, fechaInicio, fechaFin);
    }

    //te devuelve todos los registros de una aula entre dos fechas por NOMBRE
    @GetMapping("/aula/{nombreAula}/registros")
    public List<Registros> getRegistrosByAulaAndFecha(@PathVariable String nombreAula, @RequestParam Date fechaInicio, @RequestParam Date fechaFin) {
        Aulas aula = aulasRepository.findByNomAula(nombreAula);
        return registrosRepository.findByAulasAndFechaBetween(aula, fechaInicio, fechaFin);
    }

}