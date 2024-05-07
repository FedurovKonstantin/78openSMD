package com.example.weather

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.weather.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val weatherModel = WeatherModel()
        val viewModel = WeatherViewModel(weatherModel, this)

        binding.button.setOnClickListener {
            val city = binding.editText.text.toString()
            viewModel.locationByCity(city)
        }

        setSupportActionBar(findViewById(R.id.my_toolbar))

    }
}