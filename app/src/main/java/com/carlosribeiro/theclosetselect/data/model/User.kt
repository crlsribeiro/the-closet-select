package com.carlosribeiro.theclosetselect.data.model

data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val birthDate: String = "",
    val zodiacSign: String = "",
    val createdAt: Long = System.currentTimeMillis()
)