package com.udacity.project4.locationreminders.savereminder

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.reminderslist.RemindersListViewModel
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@Config(sdk = intArrayOf(Build.VERSION_CODES.P))
class SaveReminderViewModelTest {

    //TODO: provide testing to the SaveReminderView and its live data objects
    private lateinit var saveReminderViewModel: SaveReminderViewModel
    lateinit var reminders:MutableList<ReminderDTO>
    lateinit var reminderDataSource: FakeDataSource

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel() {
        //initialize reminder list
        reminders = mutableListOf()
        reminderDataSource = FakeDataSource(reminders)
        //given a save reminder ViewModel
        saveReminderViewModel =
            SaveReminderViewModel(ApplicationProvider.getApplicationContext(), reminderDataSource)
    }
    @Test
    fun save_new_reminder()= runBlockingTest {
        //Arrange reminder
        val reminder1 = ReminderDTO("title1","desc","loc1",1.1,21.2)
        //when save reminder
        reminderDataSource.saveReminder(reminder1)
        //then data equal each other
        assertNotNull(reminderDataSource.getReminder(reminder1.id))
    }

}