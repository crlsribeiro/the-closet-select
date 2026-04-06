package com.carlosribeiro.theclosetselect.domain.model

data class PaletteResult(
    val userId: String = "",
    val seasonType: String = "",
    val subtom: String = "",
    val descricao: String = "",
    val bestColors: List<PaletteColor> = emptyList(),
    val colorsToAvoid: List<PaletteColor> = emptyList(),
    val createdAt: Long = System.currentTimeMillis()
)

data class PaletteColor(
    val nome: String = "",
    val hex: String = ""
)