package com.carlosribeiro.theclosetselect.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import com.carlosribeiro.theclosetselect.data.model.GarmentDto
import com.carlosribeiro.theclosetselect.domain.model.Garment
import com.carlosribeiro.theclosetselect.domain.repository.GarmentRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.util.UUID

private const val COLLECTION_GARMENTS = "garments"
private const val STORAGE_PATH_GARMENTS = "garments"
private const val MAX_IMAGE_SIZE = 1024
private const val COMPRESSION_QUALITY = 75

class GarmentRepositoryImpl(
    private val context: Context,
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()
) : GarmentRepository {

    override fun getGarments(userId: String): Flow<List<Garment>> = callbackFlow {
        val listener = firestore
            .collection(COLLECTION_GARMENTS)
            .whereEqualTo("userId", userId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val garments = snapshot?.documents
                    ?.mapNotNull { it.toObject(GarmentDto::class.java)?.toDomain() }
                    ?: emptyList()

                trySend(garments)
            }

        awaitClose { listener.remove() }
    }

    override suspend fun saveGarment(garment: Garment, photoUri: Uri): Result<Unit> {
        return try {
            val garmentId = UUID.randomUUID().toString()
            val photoUrl = uploadPhoto(garmentId, photoUri)

            val dto = GarmentDto.fromDomain(
                garment.copy(id = garmentId, photoUrl = photoUrl)
            )

            firestore
                .collection(COLLECTION_GARMENTS)
                .document(garmentId)
                .set(dto)
                .await()

            Result.success(Unit)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    override suspend fun deleteGarment(garmentId: String): Result<Unit> {
        return try {
            firestore
                .collection(COLLECTION_GARMENTS)
                .document(garmentId)
                .delete()
                .await()

            Result.success(Unit)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    private suspend fun uploadPhoto(garmentId: String, photoUri: Uri): String =
        withContext(Dispatchers.IO) {
            val ref = storage.reference
                .child(STORAGE_PATH_GARMENTS)
                .child("$garmentId.jpg")

            val compressedBytes = compressImage(photoUri)

            ref.putBytes(compressedBytes).await()

            ref.downloadUrl.await().toString()
        }

    private fun compressImage(photoUri: Uri): ByteArray {
        // Decodifica só o tamanho da imagem primeiro
        val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        context.contentResolver.openInputStream(photoUri)?.use { stream ->
            BitmapFactory.decodeStream(stream, null, options)
        }

        // Calcula o inSampleSize para reduzir memória
        var sampleSize = 1
        var width = options.outWidth
        var height = options.outHeight
        while (width > MAX_IMAGE_SIZE * 2 || height > MAX_IMAGE_SIZE * 2) {
            sampleSize *= 2
            width /= 2
            height /= 2
        }

        // Decodifica com sample size
        val decodeOptions = BitmapFactory.Options().apply { inSampleSize = sampleSize }
        val bitmap = context.contentResolver.openInputStream(photoUri)?.use { stream ->
            BitmapFactory.decodeStream(stream, null, decodeOptions)
        } ?: throw Exception("Could not decode image")

        // Corrige rotação EXIF
        val rotatedBitmap = fixExifRotation(bitmap, photoUri)

        // Redimensiona se ainda for maior que MAX_IMAGE_SIZE
        val scaledBitmap = scaleBitmap(rotatedBitmap)

        // Comprime para JPEG
        val outputStream = ByteArrayOutputStream()
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, COMPRESSION_QUALITY, outputStream)

        if (scaledBitmap != bitmap) scaledBitmap.recycle()
        if (rotatedBitmap != bitmap) rotatedBitmap.recycle()
        bitmap.recycle()

        return outputStream.toByteArray()
    }

    private fun scaleBitmap(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height

        if (width <= MAX_IMAGE_SIZE && height <= MAX_IMAGE_SIZE) return bitmap

        val ratio = width.toFloat() / height.toFloat()
        val newWidth: Int
        val newHeight: Int

        if (width > height) {
            newWidth = MAX_IMAGE_SIZE
            newHeight = (MAX_IMAGE_SIZE / ratio).toInt()
        } else {
            newHeight = MAX_IMAGE_SIZE
            newWidth = (MAX_IMAGE_SIZE * ratio).toInt()
        }

        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    }

    private fun fixExifRotation(bitmap: Bitmap, uri: Uri): Bitmap {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return bitmap
            val exif = ExifInterface(inputStream)
            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
            inputStream.close()

            val rotation = when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90f
                ExifInterface.ORIENTATION_ROTATE_180 -> 180f
                ExifInterface.ORIENTATION_ROTATE_270 -> 270f
                else -> return bitmap
            }

            val matrix = Matrix().apply { postRotate(rotation) }
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        } catch (e: Exception) {
            bitmap
        }
    }
}