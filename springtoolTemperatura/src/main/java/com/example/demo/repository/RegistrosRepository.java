package com.example.demo.repository;

import com.example.demo.bean.Aulas;
import com.example.demo.bean.Registros;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface RegistrosRepository extends JpaRepository<Registros, Integer> {
    List<Registros> findByAulas(Aulas aulas);
    List<Registros> findByAulasAndFechaBetween(Aulas aulas, Date fechaInicio, Date fechaFin);
    List<Registros> findByFechaBetween(Date fecha, Date fecha2);
}
