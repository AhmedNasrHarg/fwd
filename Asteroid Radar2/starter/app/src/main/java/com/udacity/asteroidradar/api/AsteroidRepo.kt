package com.udacity.asteroidradar.api

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.main.lifecycleowner
import kotlinx.coroutines.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AsteroidRepo(val context: Context)  {

    var network=NetworkData()
    var asteroid = MutableLiveData<MutableList<Asteroid>>()
    var img = MutableLiveData<PictureOfDay>()

    init {
        network.asteroidList.observe(lifecycleowner, Observer { asteroids->
            asteroid.value=asteroids
        })
        network.imageOfDay.observe(lifecycleowner, Observer { image->
            img.value=image
        })
    }
    @RequiresApi(Build.VERSION_CODES.O)
     fun getData(){
        val db = AsteroidDatabase.getInstance(context).asteroidDao()
        if(isOnline(context)){
            //get data from network
            CoroutineScope(Dispatchers.IO).launch {
                async {
                    network.getAsteroid()
                }.await()
                //todo:save to room
                async {
                        db.insertMutableAsteroids(asteroid.value!!)
                }
            }
        }else{
            //todo:get data from room
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val formatted = current.format(formatter)
            println("dateeeeeeeeeeeee: $formatted")
            CoroutineScope(Dispatchers.IO).launch {
                    val res=db.getAllAsteroids(formatted)
                    withContext(Dispatchers.Main){
                        asteroid.value=res
                    }
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.M)
     fun getImageOfDay(){
            if(isOnline(context)){
                //get data from network
                CoroutineScope(Dispatchers.IO).launch {
                    async {
                      network.loadImageOfDay()
                    }.await()
                }
                //todo:save to room

            }else{
                //todo:get data from room

            }
    }
    @RequiresApi(Build.VERSION_CODES.M)
    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }
}