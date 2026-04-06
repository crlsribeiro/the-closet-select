package com.carlosribeiro.theclosetselect.domain.usecase

import com.carlosribeiro.theclosetselect.domain.model.Garment
import com.carlosribeiro.theclosetselect.domain.repository.GarmentRepository
import kotlinx.coroutines.flow.Flow

class GetGarmentsUseCase(
    private val repository: GarmentRepository
) {
    operator fun invoke(userId: String): Flow<List<Garment>> =
        repository.getGarments(userId)
}