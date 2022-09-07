package com.udacity.asteroidradar.api

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.CoroutineWorker
import androidx.work.Operation.SUCCESS
import androidx.work.WorkerParameters
import retrofit2.HttpException

class CacheAsteroidsWorker(val appContext: Context, params: WorkerParameters):
    CoroutineWorker(appContext, params) {
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun doWork(): Result {
        val repository = AsteroidRepo(appContext)

        return try {
            repository.getData()
            Result.success()		//you can return succes, if no issue
        } catch (e: HttpException) {
            Result.retry()			//or retry another time, if there are issues
        }
    }

}