package com.example.demo.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "Configuracion")
public class Configuracion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CONFIG")
    private int idConfig;

    // Relación con Usuario
    @OneToOne
    @JoinColumn(name = "ID_USER")
    @JsonIgnore
    private Usuario usuario;

    @Column(name = "COLOR_FRIO", length = 7, columnDefinition = "varchar(7) default '#1C3AFF'")
    private String colorFrio;

    @Column(name = "COLOR_OPTIMO", length = 7, columnDefinition = "varchar(7) default '#00FF00'")
    private String colorOptimo;

    @Column(name = "COLOR_CALOR", length = 7, columnDefinition = "varchar(7) default '#FF0000'")
    private String colorCalor;

    @Column(name = "NOT_FRIO", columnDefinition = "float default 18.5")
    private float notFrio;

    @Column(name = "NOT_CALOR", columnDefinition = "float default 23.5")
    private float notCalor;

    @Column(name = "T_FRIO", columnDefinition = "float default 18.5")
    private float tFrio;

    @Column(name = "T_OPTIMA_MIN", columnDefinition = "float default 18.5")
    private float tOptimaMin;

    @Column(name = "T_OPTIMA_MAX", columnDefinition = "float default 23.5")
    private float tOptimaMax;

    @Column(name = "T_CALOR", columnDefinition = "float default 23.5")
    private float tCalor;

    public Configuracion() {
        this.colorFrio = "#1C3AFF";
        this.colorOptimo = "#00FF00";
        this.colorCalor = "#FF0000";
        this.notFrio = 18.5f;
        this.notCalor = 23.5f;
        this.tFrio = 18.5f;
        this.tOptimaMin = 18.5f;
        this.tOptimaMax = 23.5f;
        this.tCalor = 23.5f;
    }
    // Constructor, getters y setters

    public int getIdConfig() {
        return idConfig;
    }

    public void setIdConfig(int idConfig) {
        this.idConfig = idConfig;
    }

    public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getColorFrio() {
        return colorFrio;
    }

    public void setColorFrio(String colorFrio) {
        this.colorFrio = colorFrio;
    }

    public String getColorOptimo() {
        return colorOptimo;
    }

    public void setColorOptimo(String colorOptimo) {
        this.colorOptimo = colorOptimo;
    }

    public String getColorCalor() {
        return colorCalor;
    }

    public void setColorCalor(String colorCalor) {
        this.colorCalor = colorCalor;
    }

    public float getNotFrio() {
        return notFrio;
    }

    public void setNotFrio(float notFrio) {
        this.notFrio = notFrio;
    }

    public float getNotCalor() {
        return notCalor;
    }

    public void setNotCalor(float notCalor) {
        this.notCalor = notCalor;
    }

    public float gettFrio() {
        return tFrio;
    }

    public void settFrio(float tFrio) {
        this.tFrio = tFrio;
    }

    public float gettOptimaMin() {
        return tOptimaMin;
    }

    public void settOptimaMin(float tOptimaMin) {
        this.tOptimaMin = tOptimaMin;
    }

    public float gettOptimaMax() {
        return tOptimaMax;
    }

    public void settOptimaMax(float tOptimaMax) {
        this.tOptimaMax = tOptimaMax;
    }

    public float gettCalor() {
        return tCalor;
    }

    public void settCalor(float tCalor) {
        this.tCalor = tCalor;
    }
    public int getUsuarioId() {
        return this.usuario.getIdUser();
    }
}
