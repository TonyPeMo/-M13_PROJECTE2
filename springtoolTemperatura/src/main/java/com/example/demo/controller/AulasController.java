package com.example.demo.controller;

import com.example.demo.bean.Aulas;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.bean.Registros;
import com.example.demo.repository.AulasRepository;
import com.example.demo.repository.RegistrosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/aulas")
public class AulasController {

    private final RegistrosRepository registrosRepository;

    private final AulasRepository aulasRepository;

    public AulasController(RegistrosRepository registrosRepository, AulasRepository aulasRepository) {
        this.registrosRepository = registrosRepository;
        this.aulasRepository = aulasRepository;
    }

    // guarda un registro en una aula por ID
    @PostMapping("/{id}/registros")
    public Registros createRegistro(@PathVariable Integer id, @RequestBody Registros registro) {
        Aulas aula = aulasRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aula", "id", id));
        registro.setAulas(aula);
        return registrosRepository.save(registro);
    }

    // guarda un registro en una aula por NOMBRE
    @PostMapping("/{nombre}/registros")
    public Registros createRegistro(@PathVariable String nombre, @RequestBody Registros registro) {
        Aulas aula = aulasRepository.findByNomAula(nombre);
        registro.setAulas(aula);
        return registrosRepository.save(registro);
    }

    //te devuelve todos los registros de una aula por ID
    @GetMapping("/{idAula}")
    public List<Registros> getRegistrosByAula(@PathVariable Integer idAula){
        Aulas aula = aulasRepository.findById(idAula)
                .orElseThrow(() -> new ResourceNotFoundException("Aula", "id", idAula));
        return registrosRepository.findByAulas(aula);
    }

    //te devuelve todos los registros de una aula POR NOMBRE
    @GetMapping("/{nombre}")
    public List<Registros> getRegistrosByAula(@PathVariable String nombre) {
        Aulas aula = aulasRepository.findByNomAula(nombre);
        return registrosRepository.findByAulas(aula);
    }

    //te devuelve todos los registros de una aula entre dos fechas por ID
    @GetMapping("/{idAula}/registros")
    public List<Registros> getRegistrosByAulaAndFecha(@PathVariable Integer idAula, @RequestParam Date fechaInicio, @RequestParam Date fechaFin) {
        Aulas aula = aulasRepository.findById(idAula)
                .orElseThrow(() -> new ResourceNotFoundException("Aula", "id", idAula));
        return registrosRepository.findByAulasAndFechaBetween(aula, fechaInicio, fechaFin);
    }

    //te devuelve todos los registros de una aula entre dos fechas por NOMBRE
    @GetMapping("/{nombre}/registros")
    public List<Registros> getRegistrosByAulaAndFecha(@PathVariable String nombre, @RequestParam Date fechaInicio, @RequestParam Date fechaFin) {
        Aulas aula = aulasRepository.findByNomAula(nombre);
        return registrosRepository.findByAulasAndFechaBetween(aula, fechaInicio, fechaFin);
    }


    //no se va a usar en teoria
    // actualiza un registro de una aula
    @PutMapping("/{idAula}/registros/{idRegistro}")
    public Registros updateRegistro(@PathVariable Integer idAula, @PathVariable Integer idRegistro, @RequestBody Registros registroDetails) {
        Aulas aula = aulasRepository.findById(idAula)
                .orElseThrow(() -> new ResourceNotFoundException("Aula", "id", idAula));
        Registros registro = registrosRepository.findById(idRegistro)
                .orElseThrow(() -> new ResourceNotFoundException("Registro", "id", idRegistro));
        registro.setAulas(aula);
        registro.setFecha(registroDetails.getFecha());
        return registrosRepository.save(registro);
    }

    //no se va a usar en teoria
    // elimina un registro de una aula
    @DeleteMapping("/{idAula}/registros/{idRegistro}")
    public void deleteRegistro(@PathVariable Integer idAula, @PathVariable Integer idRegistro) {
        Registros registro = registrosRepository.findById(idRegistro)
                .orElseThrow(() -> new ResourceNotFoundException("Registro", "id", idRegistro));
        registrosRepository.delete(registro);
    }
}