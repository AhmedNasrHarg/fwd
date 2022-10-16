package com.udacity.project4.locationreminders.data

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.local.RemindersDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Unit test the DAO
@SmallTest
@Config(sdk = intArrayOf(Build.VERSION_CODES.P))
class RemindersDaoTest {

    //    TODO: Add testing implementation to the RemindersDao.kt
// Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    private lateinit var database: RemindersDatabase
    @Before
    fun initDb() {
        // Using an in-memory database so that the information stored here disappears when the
        // process is killed.
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RemindersDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun closeDb() = database.close()

    @Test
    fun saveReminderAndGetReminderById() = runBlockingTest {
        // GIVEN - saving a reminder.
        val reminder = ReminderDTO("title", "description","location",31.1,32.3)
        database.reminderDao().saveReminder(reminder)

        // WHEN - Get the reminder by id from the database.
        val loaded = database.reminderDao().getReminderById(reminder.id)

        // THEN - The loaded data contains the expected values.
        MatcherAssert.assertThat<ReminderDTO>(loaded as ReminderDTO, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(loaded.id, CoreMatchers.`is`(reminder.id))
        MatcherAssert.assertThat(loaded.title, CoreMatchers.`is`(reminder.title))
        MatcherAssert.assertThat(loaded.description, CoreMatchers.`is`(reminder.description))
        MatcherAssert.assertThat(loaded.location, CoreMatchers.`is`(reminder.location))
        MatcherAssert.assertThat(loaded.longitude, CoreMatchers.`is`(reminder.longitude))
        MatcherAssert.assertThat(loaded.latitude, CoreMatchers.`is`(reminder.latitude))
    }
}