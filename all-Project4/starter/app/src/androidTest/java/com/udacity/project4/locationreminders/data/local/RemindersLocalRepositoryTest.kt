package com.udacity.project4.locationreminders.data.local

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Medium Test to test the repository
@MediumTest
@Config(sdk = intArrayOf(Build.VERSION_CODES.P))
class RemindersLocalRepositoryTest(var reminders :MutableList<ReminderDTO>) : ReminderDataSource {

    //    TODO: Add testing implementation to the RemindersLocalRepository.kt
    private var shouldReturnError = false
    fun setReturnError(value:Boolean){
        shouldReturnError=value
    }

    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        return try {
            Result.Success(reminders)
        } catch (ex: Exception) {
            Result.Error(ex.localizedMessage)
        }
    }

    override suspend fun saveReminder(reminder: ReminderDTO) {
        //TODO("save the reminder")
        reminders.add(reminder)
    }

    override suspend fun getReminder(id: String): Result<ReminderDTO> {
        //TODO("return the reminder with the id")
        if (shouldReturnError) {
            return Result.Error("Test exception")
        }
        return try {
            val remider:ReminderDTO = reminders.find { it.id == id  }!!
            Result.Success(remider)
        }catch (ex:java.lang.Exception){
            Result.Error(ex.localizedMessage)
        }
    }

    override suspend fun deleteAllReminders() {
        //TODO("delete all the reminders")
        reminders.clear()
    }
}