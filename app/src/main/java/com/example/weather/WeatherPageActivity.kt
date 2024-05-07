package com.example.weather

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class WeatherPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_page)
        setSupportActionBar(findViewById(R.id.my_toolbar))

        supportActionBar?.setDisplayHomeAsUpEnabled(true)





        findViewById<TextView>(R.id.textView3).text =
            "Название города: ${intent.getStringExtra("cityName")}"

        findViewById<TextView>(R.id.textView5).text =
            "Скорость ветра: ${intent.getStringExtra("windspeed")}"

        findViewById<TextView>(R.id.textView4).text =
            "Температура: ${intent.getStringExtra("temperature")}"


    }
}