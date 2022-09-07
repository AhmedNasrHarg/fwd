package com.udacity.asteroidradar.api

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit
import javax.security.auth.callback.Callback

class NetworkData {

    var asteroidList = MutableLiveData<MutableList<Asteroid>>()
    var imageOfDay = MutableLiveData<PictureOfDay>()

    init {
        asteroidList.value = mutableListOf()
    }
    fun loadImageOfDay(){
        GlobalScope.launch {

        var getImageDeferred = NasaApi.retrofitService.getImageOfDay()
            try {
                var result = getImageDeferred.await()
                if (result.mediaType == "image"){
                imageOfDay.value = result
                    println("hyhyyhyhyhyhy   ${imageOfDay.value}")
                    print("kjj")
                }
            }catch (e: Exception) {

            }

        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun getAsteroid(){

        GlobalScope.launch {
            println("1................")
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val startDate = current.format(formatter)
            val endDate = current.plusDays(7).format(formatter)
            println("dateeeeeeeeeeeeenddddd: $endDate")
            var asteroidDeferred = NasaApi.retrofitService.getWeekAsteroids(startDate = startDate ,endDate = endDate )
            println("2................")
//            try {
                println("3................")
                var result = asteroidDeferred.await()
                println("4................")
            withContext(Dispatchers.Main){
                asteroidList.value = parseAsteroidsJsonResult(JSONObject(result))
            }
                println("5................")
                println("hyhyyhyhyhyhy   ${asteroidList.value}")
//            }catch (e: Exception) {
////                asteroidList.value =
//                println(e.message)
//            }
        }
        println("hi1234444 ${asteroidList.value}")
    }
}
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()
private val client = OkHttpClient.Builder()
    .connectTimeout(300,TimeUnit.SECONDS)
    .readTimeout(300,TimeUnit.SECONDS)
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(Constants.BASE_URL)
    .client(client)
    .build()

interface NasaApiService {
    //todo: how to make it date-dynamic

    @GET("neo/rest/v1/feed?api_key=5X4KzoExdFFVb2Tgj1bSxIelrlgcCvOGy1EcfWdC")
    fun getWeekAsteroids(@Query("start_date") startDate: String,@Query("end_date") endDate:String):
            Deferred<String>
    @GET("/planetary/apod?api_key=5X4KzoExdFFVb2Tgj1bSxIelrlgcCvOGy1EcfWdC")
    fun getImageOfDay():
            Deferred<PictureOfDay>
}

object NasaApi {
    val retrofitService : NasaApiService by lazy {
        retrofit.create(NasaApiService::class.java)
    }
}
