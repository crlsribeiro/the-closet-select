package com.carlosribeiro.theclosetselect.data.repository

import android.content.Context
import android.net.Uri
import com.carlosribeiro.theclosetselect.data.remote.AnthropicService
import com.carlosribeiro.theclosetselect.domain.model.Garment
import com.carlosribeiro.theclosetselect.domain.repository.GarmentIdentifierRepository

class GarmentIdentifierRepositoryImpl(
    context: Context
) : GarmentIdentifierRepository {

    private val anthropicService = AnthropicService(context)

    override suspend fun identifyGarment(photoUri: Uri): Result<Garment> =
        anthropicService.identifyGarment(photoUri)
}