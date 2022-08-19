package com.udacity.asteroidradar.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.api.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class MainViewModel : ViewModel() {

    var asteroidList = MutableLiveData<MutableList<Asteroid>>()
    var imageOfDay = MutableLiveData<String>()  //1.

    init {
        asteroidList.value = mutableListOf()
    }

    //2.
    fun loadImageOfDay(){
        NasaApi.retrofitService.getImageOfDay().enqueue(object :Callback,
            retrofit2.Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                response.body()
                println(response.body())
                imageOfDay.value = JSONObject(response.body()).get("url") as String
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    fun getAsteroid():MutableList<Asteroid>{
        NasaApi.retrofitService.getWeekAsteroids().enqueue(object :Callback,
            retrofit2.Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                asteroidList.value = parseAsteroidsJsonResult(JSONObject(response.body().toString()))
                println(asteroidList.value)
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                asteroidList.value = mutableListOf()
            }
        })
        return asteroidList.value!!
    }
}
