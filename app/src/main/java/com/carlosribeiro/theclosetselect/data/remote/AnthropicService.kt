package com.carlosribeiro.theclosetselect.data.remote

import android.content.Context
import android.net.Uri
import android.util.Base64
import com.carlosribeiro.theclosetselect.BuildConfig
import com.carlosribeiro.theclosetselect.domain.model.Garment
import com.carlosribeiro.theclosetselect.domain.model.GarmentCategory
import com.carlosribeiro.theclosetselect.domain.model.GarmentType
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit

class AnthropicService(private val context: Context) {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .build()

    private val json = Json { ignoreUnknownKeys = true }

    suspend fun identifyGarment(photoUri: Uri): Result<Garment> {
        return try {
            val base64Image = encodeImageToBase64(photoUri)
                ?: return Result.failure(Exception("Failed to read image"))

            val requestBody = buildRequestBody(base64Image)
            val response = executeRequest(requestBody)
            val garment = parseResponse(response)
            Result.success(garment)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    private fun encodeImageToBase64(uri: Uri): String? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val bytes = inputStream?.readBytes()
            inputStream?.close()
            bytes?.let { Base64.encodeToString(it, Base64.NO_WRAP) }
        } catch (exception: Exception) {
            null
        }
    }

    private fun buildRequestBody(base64Image: String): String {
        val prompt = """
            Analyze this clothing item and respond ONLY with a valid JSON object in this exact format:
            {
              "name": "Item name (e.g. Silk Blouse, Wool Coat)",
              "category": "ONE of: OUTERWEAR, ESSENTIALS, EVENING, CASUAL, FORMAL, ALL"
            }
            Do not include any text outside the JSON.
        """.trimIndent()

        val body = JsonObject(
            mapOf(
                "model" to JsonPrimitive("claude-opus-4-5"),
                "max_tokens" to JsonPrimitive(256),
                "messages" to JsonArray(
                    listOf(
                        JsonObject(
                            mapOf(
                                "role" to JsonPrimitive("user"),
                                "content" to JsonArray(
                                    listOf(
                                        JsonObject(
                                            mapOf(
                                                "type" to JsonPrimitive("image"),
                                                "source" to JsonObject(
                                                    mapOf(
                                                        "type" to JsonPrimitive("base64"),
                                                        "media_type" to JsonPrimitive("image/jpeg"),
                                                        "data" to JsonPrimitive(base64Image)
                                                    )
                                                )
                                            )
                                        ),
                                        JsonObject(
                                            mapOf(
                                                "type" to JsonPrimitive("text"),
                                                "text" to JsonPrimitive(prompt)
                                            )
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )

        return body.toString()
    }

    private fun executeRequest(requestBody: String): String {
        val request = Request.Builder()
            .url("https://api.anthropic.com/v1/messages")
            .addHeader("x-api-key", BuildConfig.ANTHROPIC_API_KEY)
            .addHeader("anthropic-version", "2023-06-01")
            .addHeader("content-type", "application/json")
            .post(requestBody.toRequestBody("application/json".toMediaType()))
            .build()

        val response = client.newCall(request).execute()
        val body = response.body?.string() ?: throw Exception("Empty response from Anthropic")

        if (!response.isSuccessful) {
            throw Exception("Anthropic API error ${response.code}: $body")
        }

        return body
    }

    private fun parseResponse(responseBody: String): Garment {
        val root = json.parseToJsonElement(responseBody).jsonObject
        val text = root["content"]
            ?.jsonArray
            ?.firstOrNull()
            ?.jsonObject
            ?.get("text")
            ?.jsonPrimitive
            ?.content
            ?: throw Exception("Could not parse Anthropic response")

        val garmentJson = json.parseToJsonElement(text).jsonObject

        val name = garmentJson["name"]?.jsonPrimitive?.content ?: "Unknown Garment"

        val category = garmentJson["category"]?.jsonPrimitive?.content
            ?.let { value -> GarmentCategory.entries.firstOrNull { it.name == value } }
            ?: GarmentCategory.ALL

        return Garment(
            name = name,
            category = category,
            type = GarmentType.BLUSA
        )
    }
}