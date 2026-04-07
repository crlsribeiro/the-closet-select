package com.carlosribeiro.theclosetselect.data.model

data class WeatherResponse(
    val cityName: String = "",
    val tempC: Double = 0.0,
    val condition: String = "",
    val conditionCode: Int = 0,
    val humidity: Int = 0,
    val feelsLikeC: Double = 0.0,
    val isDay: Boolean = true
) {
    val weatherEmoji: String
        get() = when (conditionCode) {
            1000 -> if (isDay) "☀️" else "🌙"
            1003 -> "⛅"
            1006, 1009 -> "☁️"
            1030, 1135, 1147 -> "🌫️"
            1063, 1150, 1153, 1180, 1183 -> "🌦️"
            1066, 1210, 1213 -> "🌨️"
            1087, 1273, 1276 -> "⛈️"
            1114, 1117 -> "🌬️"
            1186, 1189, 1192, 1195 -> "🌧️"
            1219, 1222, 1225 -> "❄️"
            1240, 1243, 1246 -> "🌧️"
            else -> if (isDay) "☀️" else "🌙"
        }

    val tempFormatted: String get() = "${tempC.toInt()}°C"
    val feelsLikeFormatted: String get() = "Sensação ${feelsLikeC.toInt()}°C"
}