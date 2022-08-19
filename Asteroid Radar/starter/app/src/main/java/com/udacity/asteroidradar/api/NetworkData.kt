package com.udacity.asteroidradar.api

import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

class NetworkData {
}
private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(Constants.BASE_URL)
    .build()

interface NasaApiService {
    @GET("neo/rest/v1/feed?start_date=2022-08-19&end_date=2022-08-25&api_key=5X4KzoExdFFVb2Tgj1bSxIelrlgcCvOGy1EcfWdC")
    fun getWeekAsteroids():
            Call<String>
    @GET("/planetary/apod?api_key=5X4KzoExdFFVb2Tgj1bSxIelrlgcCvOGy1EcfWdC")
    fun getImageOfDay():
            Call<String>
}

object NasaApi {
    val retrofitService : NasaApiService by lazy {
        retrofit.create(NasaApiService::class.java)
    }
}
