package com.udacity.project4.locationreminders.data

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import com.udacity.project4.locationreminders.data.local.RemindersDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.withContext
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

//Use FakeDataSource that acts as a test double to the LocalDataSource
class FakeDataSource(var reminders :MutableList<ReminderDTO>) : ReminderDataSource {

//    TODO: Create a fake data source to act as a double to the real data source
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

