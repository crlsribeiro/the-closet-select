package com.carlosribeiro.theclosetselect.domain.repository

import android.net.Uri
import com.carlosribeiro.theclosetselect.domain.model.Garment

interface GarmentIdentifierRepository {
    suspend fun identifyGarment(photoUri: Uri): Result<Garment>
}