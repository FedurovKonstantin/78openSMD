package com.example.weather

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity

class WeatherViewModel(val model: WeatherModel, val context: Context) {

    init {
        model.initDb(context)
        model.initRetrofit()
    }

    fun locationByCity(city: String) {
        model.locationByCity(city) {
            var cityName: String

            if (it == null) {
                val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                builder
                    .setMessage("Не удалось загрузить город. Попробуйте другое название или проверьте соединение с сетью.")
                    .setTitle("Загрузка города")
                    .setPositiveButton("Закрыть") { dialog, which ->
                    }

                val dialog: AlertDialog = builder.create()
                dialog.show()
                return@locationByCity
            }
            cityName = it.name

            model.weatherByLocation(it) {

                if (it == null) {

                    val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                    builder
                        .setMessage("Не удалось загрузить погоду. Попробуйте другое название или проверьте соединение с сетью.")
                        .setTitle("Загрузка погоды")
                        .setPositiveButton("Закрыть") { dialog, which ->
                        }

                    val dialog: AlertDialog = builder.create()
                    dialog.show()
                    return@weatherByLocation

                }

                startActivity(
                    context,
                    Intent(
                        context,
                        WeatherPageActivity::class.java
                    ).putExtra("cityName", cityName).putExtra(
                        "temperature",
                        "${it.currentWeather.temperature}${it.currentWeatherUnits.temperature}"
                    ).putExtra(
                        "windspeed",
                        "${it.currentWeather.windspeed}${it.currentWeatherUnits.windspeed}"
                    ),
                    null
                )


            }
        }
    }
}