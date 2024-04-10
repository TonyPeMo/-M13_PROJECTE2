package com.example.temperatura.data

data class ConfiguracionData(
    val id: Int,
    val idUsuario: Int,
    val colorFrio: String = "#1C3AFF",
    val colorOptimo: String = "#00FF00",
    val colorCalor: String = "#FF0000",
    val notFrio: Float = 18.5f,
    val notCalor: Float = 23.5f,
    val tFrio: Float = 18.5f,
    val tOptimaMin: Float = 18.5f,
    val tOptimaMax: Float = 23.5f,
    val tCalor: Float = 23.5f
)