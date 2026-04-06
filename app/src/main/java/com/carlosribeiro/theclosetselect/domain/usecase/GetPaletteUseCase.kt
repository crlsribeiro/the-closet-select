package com.carlosribeiro.theclosetselect.domain.usecase

import com.carlosribeiro.theclosetselect.domain.model.PaletteResult
import com.carlosribeiro.theclosetselect.domain.repository.PaletteRepository

class GetPaletteUseCase(
    private val repository: PaletteRepository
) {
    suspend operator fun invoke(userId: String): PaletteResult? =
        repository.getPalette(userId)
}