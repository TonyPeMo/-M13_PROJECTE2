package com.example.demo.bean;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "Aulas")
public class Aulas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_AULA")
    private int idAula;

    @Column(name = "NOM_AULA", nullable = false, unique = true, length = 255)
    private String nomAula;

    @Column(name = "NUM_PLANTA", nullable = false)
    private int numPlanta;

    // Relacion con Configuracion
    @OneToMany(mappedBy = "aulas", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Registros> Registros;

    // Constructor, getters y setters
    public List<Registros> getRegistros() {
        return Registros;
    }

    public void setRegistros(List<Registros> registros) {
        Registros = registros;
    }

    public int getIdAula() {
        return this.idAula;
    }

    public void setIdAula(int idAula) {
        this.idAula = idAula;
    }

    public String getNomAula() {
        return nomAula;
    }

    public void setNomAula(String nomAula) {
        this.nomAula = nomAula;
    }

    public int getNumPlanta() {
        return numPlanta;
    }

    public void setNumPlanta(int numPlanta) {
        this.numPlanta = numPlanta;
    }
}