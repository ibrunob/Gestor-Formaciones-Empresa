package dev.brunob.appfe.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Student(
    val id: String,
    val email: String,
    val password: String,
    val nombre: String,
    val apellidos: String,
    val tutorEmpresa: String = "",
    val tutorEmpresaEmail: String = "",
    val tutorDocente: String = "",
    val tutorDocenteEmail: String = "",
    val empresa: String = "",
    val horario: String = "",
    val fechaInicio: String = "",   // ISO yyyy-MM-dd
    val fechaFin: String = "",      // ISO yyyy-MM-dd
    val planFormacion: String = ""
)
