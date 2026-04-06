package com.carlosribeiro.theclosetselect.data.model

import com.carlosribeiro.theclosetselect.domain.model.PaletteColor
import com.carlosribeiro.theclosetselect.domain.model.PaletteResult

data class PaletteDto(
    val userId: String = "",
    val seasonType: String = "",
    val subtom: String = "",
    val descricao: String = "",
    val bestColors: List<Map<String, String>> = emptyList(),
    val colorsToAvoid: List<Map<String, String>> = emptyList(),
    val createdAt: Long = System.currentTimeMillis()
) {
    fun toDomain(): PaletteResult = PaletteResult(
        userId = userId,
        seasonType = seasonType,
        subtom = subtom,
        descricao = descricao,
        bestColors = bestColors.map { PaletteColor(nome = it["nome"] ?: "", hex = it["hex"] ?: "") },
        colorsToAvoid = colorsToAvoid.map { PaletteColor(nome = it["nome"] ?: "", hex = it["hex"] ?: "") },
        createdAt = createdAt
    )

    companion object {
        fun fromDomain(palette: PaletteResult): PaletteDto = PaletteDto(
            userId = palette.userId,
            seasonType = palette.seasonType,
            subtom = palette.subtom,
            descricao = palette.descricao,
            bestColors = palette.bestColors.map { mapOf("nome" to it.nome, "hex" to it.hex) },
            colorsToAvoid = palette.colorsToAvoid.map { mapOf("nome" to it.nome, "hex" to it.hex) },
            createdAt = palette.createdAt
        )
    }
}