package com.carlosribeiro.theclosetselect.data.remote

import android.util.Log
import com.carlosribeiro.theclosetselect.BuildConfig
import com.carlosribeiro.theclosetselect.data.model.WeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.concurrent.TimeUnit

private const val TAG = "WeatherService"
private const val BASE_URL = "https://api.weatherapi.com/v1/current.json"

class WeatherService {

    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .build()

    suspend fun getWeather(lat: Double, lon: Double): Result<WeatherResponse> =
        withContext(Dispatchers.IO) {
            try {
                val apiKey = BuildConfig.WEATHER_API_KEY
                if (apiKey.isBlank()) {
                    return@withContext Result.failure(Exception("WEATHER_API_KEY não configurada."))
                }

                val url = "$BASE_URL?key=$apiKey&q=$lat,$lon&aqi=no&lang=pt"
                Log.d(TAG, "Fetching weather for $lat,$lon")

                val request = Request.Builder().url(url).build()
                val response = client.newCall(request).execute()
                val body = response.body?.string() ?: throw Exception("Empty response")

                if (!response.isSuccessful) {
                    throw Exception("Weather API error ${response.code}: $body")
                }

                val weather = parseWeather(body)
                Log.d(TAG, "Weather: ${weather.cityName} ${weather.tempFormatted}")
                Result.success(weather)
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching weather", e)
                Result.failure(e)
            }
        }

    private fun parseWeather(json: String): WeatherResponse {
        val root = JSONObject(json)
        val location = root.getJSONObject("location")
        val current = root.getJSONObject("current")
        val condition = current.getJSONObject("condition")

        return WeatherResponse(
            cityName = location.getString("name"),
            tempC = current.getDouble("temp_c"),
            condition = condition.getString("text"),
            conditionCode = condition.getInt("code"),
            humidity = current.getInt("humidity"),
            feelsLikeC = current.getDouble("feelslike_c"),
            isDay = current.getInt("is_day") == 1
        )
    }
}