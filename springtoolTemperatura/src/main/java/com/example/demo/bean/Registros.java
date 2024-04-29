package com.example.demo.bean;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Registros")
public class Registros {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_REGISTRO")
    private int idRegistro;

    @ManyToOne
    @JoinColumn(name = "ID_AULA", nullable = false)
    private Aulas aulas;

    @Column(name = "TEMPERATURA", nullable = false)
    private float temperatura;

    @Column(name = "TERMOMETRO", nullable = false)
    private int termometro;

    @Column(name = "FECHA", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;


    public Aulas getAulas() {
		return aulas;
	}

	public void setAulas(Aulas aulas) {
		this.aulas = aulas;
	}

	public int getIdRegistro() {
        return idRegistro;
    }

    public void setIdRegistro(int idRegistro) {
        this.idRegistro = idRegistro;
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
