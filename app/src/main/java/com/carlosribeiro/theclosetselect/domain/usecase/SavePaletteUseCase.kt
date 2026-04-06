package com.carlosribeiro.theclosetselect.domain.usecase

import com.carlosribeiro.theclosetselect.domain.model.PaletteResult
import com.carlosribeiro.theclosetselect.domain.repository.PaletteRepository

class SavePaletteUseCase(
    private val repository: PaletteRepository
) {
    suspend operator fun invoke(palette: PaletteResult): Result<Unit> =
        repository.savePalette(palette)
}