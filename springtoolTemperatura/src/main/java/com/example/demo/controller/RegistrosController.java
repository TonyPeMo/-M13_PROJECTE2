package com.example.demo.controller;

import com.example.demo.bean.Aulas;
import com.example.demo.bean.Registros;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.AulasRepository;
import com.example.demo.repository.RegistrosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @PostMapping
    public Registros createRegistro(@RequestBody Registros registroRequest, @RequestParam Integer idAula) {
        System.out.println(registroRequest);

        // Buscar el objeto Aulas por su ID
        Aulas aula = aulasRepository.findById(idAula)
                .orElseThrow(() -> new ResourceNotFoundException("Aulas", "id", idAula));

        // Crear un nuevo registro
        Registros nuevoRegistro = new Registros();
        nuevoRegistro.setAulas(aula);
        nuevoRegistro.setTemperatura(registroRequest.getTemperatura());
        nuevoRegistro.setTermometro(registroRequest.getTermometro());
        nuevoRegistro.setFecha(registroRequest.getFecha());

        // Guardar el registro en la base de datos
        return registrosRepository.save(nuevoRegistro);
    }


    // Obtener todos los registros
    @GetMapping
    public List<Registros> getAllRegistros() {
        return registrosRepository.findAll();
    }

    @GetMapping("/{id}")
    public Aulas getAulaById(@PathVariable Integer id) {
        return aulasRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aulas", "id", id));
    }

    // Obtener todos los registros entre dos fechas
    // no se va a usar en teoria
    @GetMapping("/fecha")
    public List<Registros> getRegistrosByFecha(@RequestParam @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date fechaInicio,
                                               @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date fechaFin) {
        return registrosRepository.findByFechaBetween(fechaInicio, fechaFin);
    }


    @DeleteMapping("/{id}")
    public void deleteRegistro(@PathVariable int id) {
        registrosRepository.deleteById(id);
    }

}