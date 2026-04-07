package com.carlosribeiro.theclosetselect.data.model

data class User(
    val uid: String = "",
    val displayName: String = "",
    val email: String = "",
    val birthdate: String = "",   // ← era birthDate
    val zodiacSign: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

