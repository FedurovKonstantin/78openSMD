package com.example.weather

import android.content.Context
import androidx.room.Room
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

data class CityLocation(
    val results: List<Location>
)

data class LocationWeather(
    @SerializedName("current_weather")
    val currentWeather: CurrentWeather,

    @SerializedName("current_weather_units")
    val currentWeatherUnits: CurrentWeatherUnits
)

data class CurrentWeather(
    val temperature: Double,
    val windspeed: Double,
)

data class CurrentWeatherUnits(
    val temperature: String,
    val windspeed: String,
)

interface WeatherApi {
    @GET("https://geocoding-api.open-meteo.com/v1/search?count=1")
    fun locationByCity(
        @Query("name") city: String
    ): Call<CityLocation?>

    @GET("https://api.open-meteo.com/v1/forecast?current_weather=true")
    fun weatherByLocation(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
    ): Call<LocationWeather?>
}

class WeatherModel {


    lateinit var db: AppDatabase
    lateinit var retrofit: Retrofit
    lateinit var weatherApi: WeatherApi


    fun initRetrofit() {
        retrofit = Retrofit
            .Builder()
            .baseUrl("https://api.open-meteo.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()


        weatherApi = retrofit.create(WeatherApi::class.java)
    }

    fun initDb(context: Context) {
        db = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "weather"
        ).allowMainThreadQueries().build()
    }


    fun locationByCity(city: String, onLoaded: (Location?) -> Unit) {
        val localLocation = db.locationDao().getLocationById(city).firstOrNull()

        if (localLocation != null) {
            onLoaded(localLocation)
            return
        }

        weatherApi.locationByCity(city).enqueue(object : Callback<CityLocation?> {
            override fun onResponse(call: Call<CityLocation?>, responce: Response<CityLocation?>) {

                val location = responce.body()?.results?.firstOrNull()

                if (responce.isSuccessful && location != null) {
                    db.locationDao().insert(location)
                    onLoaded(location)
                    return
                }

                onLoaded(null)
            }

            override fun onFailure(p0: Call<CityLocation?>, p1: Throwable) {
                onLoaded(null)
            }
        })
    }

    fun weatherByLocation(location: Location, onLoaded: (LocationWeather?) -> Unit) =
        weatherApi.weatherByLocation(location.latitude, location.longitude)
            .enqueue(object : Callback<LocationWeather?> {
                override fun onResponse(
                    call: Call<LocationWeather?>,
                    responce: Response<LocationWeather?>
                ) {

                    val weather = responce.body()

                    if (responce.isSuccessful && weather != null) {
                        onLoaded(weather)
                        return
                    }

                    onLoaded(null)
                }

                override fun onFailure(p0: Call<LocationWeather?>, p1: Throwable) {
                    onLoaded(null)
                }
            })


}

