package com.udacity.asteroidradar.main

import android.app.Application
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.api.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class MainViewModel( application: Application) : AndroidViewModel(application) {

    var asteroidList = MutableLiveData<MutableList<Asteroid>>()
    var imageOfDay = MutableLiveData<PictureOfDay>()
    var asteroidRepo:AsteroidRepo = AsteroidRepo(application.applicationContext)

    init {
        asteroidList.value = mutableListOf()
        asteroidRepo.asteroid.observe(lifecycleowner, Observer { asteroids ->
            asteroidList.value=asteroids
        })
        asteroidRepo.img.observe(lifecycleowner, Observer { image ->
            imageOfDay.value=image
        })
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun loadImageOfDay(){
        CoroutineScope(Dispatchers.Main).launch {
        async { asteroidRepo.getImageOfDay()}.await()
        }
//        NasaApi.retrofitService.getImageOfDay().enqueue(object :Callback,
//            retrofit2.Callback<PictureOfDay> {
//            override fun onResponse(call: Call<PictureOfDay>, response: Response<PictureOfDay>) {
//                response.body()
//                println(response.body()!!.url)
//                if (response.body()!!.mediaType == "image")
//                    imageOfDay.value = response.body()!!.url
//               //without moshi:
//              //imageOfDay.value = JSONObject(response.body()).get("url") as String
//            }
//
//            override fun onFailure(call: Call<PictureOfDay>, t: Throwable) {
//                TODO("Not yet implemented")
//            }
//        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getAsteroid():MutableList<Asteroid>{
        CoroutineScope(Dispatchers.Main).launch {
            async { asteroidRepo.getData()}.await()
        }
//        NasaApi.retrofitService.getWeekAsteroids().enqueue(object :Callback,
//            retrofit2.Callback<String> {
//            override fun onResponse(call: Call<String>, response: Response<String>) {
//                asteroidList.value = parseAsteroidsJsonResult(JSONObject(response.body().toString()))
//                println(asteroidList.value)
//            }
//
//            override fun onFailure(call: Call<String>, t: Throwable) {
//                asteroidList.value = mutableListOf()
//            }
//        })
        return asteroidList.value!!
    }
}
