package com.example.demo.repository;

import com.example.demo.bean.Aulas;
import com.example.demo.bean.Registros;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface RegistrosRepository extends JpaRepository<Registros, Integer> {
    List<Registros> findByAulas(Aulas aulas);
    List<Registros> findByAulasAndFechaBetween(Aulas aulas, Date fechaInicio, Date fechaFin);
    List<Registros> findByFechaBetween(Date fecha, Date fecha2);
    List<Registros> findByAulasOrderByFechaDesc(Aulas aula);

    @Query(value = "SELECT ROUND(AVG(temperatura), 2) AS temperatura_media FROM Registros WHERE id_aula = :idAula AND fecha BETWEEN (SELECT MAX(fecha) FROM Registros WHERE id_aula = :idAula) - INTERVAL 5 MINUTE AND (SELECT MAX(fecha) FROM Registros WHERE id_aula = :idAula)", nativeQuery = true)
    Double getMediaTemperaturaUltimosCincoMinutos(@Param("idAula") Integer idAula);
}
