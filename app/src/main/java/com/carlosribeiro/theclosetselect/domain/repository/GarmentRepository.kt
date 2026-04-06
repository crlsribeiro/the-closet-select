package com.carlosribeiro.theclosetselect.domain.repository

import android.net.Uri
import com.carlosribeiro.theclosetselect.domain.model.Garment
import kotlinx.coroutines.flow.Flow

interface GarmentRepository {
    fun getGarments(userId: String): Flow<List<Garment>>
    suspend fun saveGarment(garment: Garment, photoUri: Uri): Result<Unit>
    suspend fun deleteGarment(garmentId: String): Result<Unit>
}