package com.example.temperatura.Model;

import java.util.Date;

public class Registro {

    private int id_registro;
    private int id_aula;
    private float temperatura;
    private int termometro;
    private Date fecha;

    public int getId_registro() {
        return id_registro;
    }

    public void setId_registro(int id_registro) {
        this.id_registro = id_registro;
    }

    public int getId_aula() {
        return id_aula;
    }

    public void setId_aula(int id_aula) {
        this.id_aula = id_aula;
    }

    public float getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(float temperatura) {
        this.temperatura = temperatura;
    }

    public int getTermometro() {
        return termometro;
    }

    public void setTermometro(int termometro) {
        this.termometro = termometro;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
