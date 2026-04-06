package com.carlosribeiro.theclosetselect.domain.usecase

import android.net.Uri
import com.carlosribeiro.theclosetselect.domain.model.Garment
import com.carlosribeiro.theclosetselect.domain.repository.GarmentRepository

class SaveGarmentUseCase(
    private val repository: GarmentRepository
) {
    suspend operator fun invoke(garment: Garment, photoUri: Uri): Result<Unit> =
        repository.saveGarment(garment, photoUri)
}