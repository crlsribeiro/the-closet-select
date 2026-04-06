package com.carlosribeiro.theclosetselect.data.repository

import com.carlosribeiro.theclosetselect.data.model.PaletteDto
import com.carlosribeiro.theclosetselect.domain.model.PaletteResult
import com.carlosribeiro.theclosetselect.domain.repository.PaletteRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

private const val COLLECTION_PALETTES = "palettes"

class PaletteRepositoryImpl(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : PaletteRepository {

    override suspend fun getPalette(userId: String): PaletteResult? {
        return try {
            val doc = firestore
                .collection(COLLECTION_PALETTES)
                .document(userId)
                .get()
                .await()

            if (doc.exists()) {
                doc.toObject(PaletteDto::class.java)?.toDomain()
            } else {
                null
            }
        } catch (exception: Exception) {
            null
        }
    }

    override suspend fun savePalette(palette: PaletteResult): Result<Unit> {
        return try {
            val dto = PaletteDto.fromDomain(palette)
            firestore
                .collection(COLLECTION_PALETTES)
                .document(palette.userId)
                .set(dto)
                .await()
            Result.success(Unit)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}