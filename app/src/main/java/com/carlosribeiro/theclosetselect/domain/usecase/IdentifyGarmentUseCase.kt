package com.carlosribeiro.theclosetselect.domain.usecase

import android.net.Uri
import com.carlosribeiro.theclosetselect.domain.model.Garment
import com.carlosribeiro.theclosetselect.domain.repository.GarmentIdentifierRepository

class IdentifyGarmentUseCase(
    private val repository: GarmentIdentifierRepository
) {
    suspend operator fun invoke(photoUri: Uri): Result<Garment> =
        repository.identifyGarment(photoUri)
}