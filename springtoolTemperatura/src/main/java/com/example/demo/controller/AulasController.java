package com.example.demo.controller;

import com.example.demo.bean.Aulas;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.bean.Registros;
import com.example.demo.repository.AulasRepository;
import com.example.demo.repository.RegistrosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;


// TODO FUNCIONA!!!!
@RestController
@RequestMapping("/aulas")
public class AulasController {

        @Autowired
        private AulasRepository aulasRepository;

        @Autowired
        private RegistrosRepository registrosRepository;

        @GetMapping
        public List<Aulas> getAulas() {
            return aulasRepository.findAll();
        }

        @GetMapping("/{id}")
        public Aulas getAula(@PathVariable int id) {
            return aulasRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Aulas" , "id", id));
        }

        @GetMapping("/nombre/{nombre}")
        public Aulas getAulaPorNombre(@PathVariable String nombre) {
            return aulasRepository.findByNomAula(nombre);
        }

        @PostMapping
        public Aulas createAula(@RequestBody Aulas aula) {
            return aulasRepository.save(aula);
        }

        @PostMapping("/list")
        public List<Aulas> createAulas(@RequestBody List<Aulas> aulasList) {
            return aulasRepository.saveAll(aulasList);
        }

        @PutMapping("/{id}")
        public Aulas updateAula(@PathVariable int id, @RequestBody Aulas aula) {
            Aulas aulaToUpdate = aulasRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Aulas" , "id", id));
            aulaToUpdate.setNomAula(aula.getNomAula());
            aulaToUpdate.setNumPlanta(aula.getNumPlanta());
            return aulasRepository.save(aulaToUpdate);
        }

        @DeleteMapping("/{id}")
        public void deleteAula(@PathVariable int id) {
            aulasRepository.deleteById(id);
        }

        @GetMapping("/{id}/registros")
        public List<Registros> getRegistros(@PathVariable int id) {
            Aulas aula = aulasRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Aulas" , "id", id));
            return aula.getRegistros();
        }

        @PostMapping("/{id}/registros")
        public Registros createRegistro(@PathVariable int id, @RequestBody Registros registro) {
            Aulas aula = aulasRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Aulas" , "id", id));
            registro.setAulas(aula);
            registro.setFecha(new Date());
            return registrosRepository.save(registro);
        }

    @GetMapping("/nombre/{nombre}/registros")
    public List<Registros> getRegistros(@PathVariable String nombre) {
        Aulas aula = aulasRepository.findByNomAula(nombre);
        if (aula == null) {
            throw new ResourceNotFoundException("Aulas", "nombre", nombre);
        }
        return aula.getRegistros();
    }

    @PostMapping("/nombre/{nombre}/registros")
    public Registros createRegistro(@PathVariable String nombre, @RequestBody Registros registro) {
        Aulas aula = aulasRepository.findByNomAula(nombre);
        if (aula == null) {
            throw new ResourceNotFoundException("Aulas", "nombre", nombre);
        }
        registro.setAulas(aula);
        registro.setFecha(new Date());
        return registrosRepository.save(registro);
    }
}