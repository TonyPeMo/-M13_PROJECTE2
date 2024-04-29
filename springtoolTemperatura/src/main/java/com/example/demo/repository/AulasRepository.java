package com.example.demo.repository;

import com.example.demo.bean.Aulas;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AulasRepository extends JpaRepository<Aulas, Integer>{
    Aulas findByNomAula(String nombre);
    List<Aulas> findByNumPlanta(int numPlanta);
}