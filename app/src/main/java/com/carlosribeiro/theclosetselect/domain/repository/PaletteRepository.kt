package com.carlosribeiro.theclosetselect.domain.repository

import com.carlosribeiro.theclosetselect.domain.model.PaletteResult

interface PaletteRepository {
    suspend fun getPalette(userId: String): PaletteResult?
    suspend fun savePalette(palette: PaletteResult): Result<Unit>
}