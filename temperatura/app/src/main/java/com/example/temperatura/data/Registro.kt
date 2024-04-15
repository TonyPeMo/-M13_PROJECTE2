package com.example.temperatura.data

import java.util.Date

data class Registro(
    val id: Int,
    val idAula: Int,
    val temperatura: Float,
    val termometro: Int,
    val fecha: Date
)