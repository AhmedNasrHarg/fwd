package com.udacity.project4.locationreminders.reminderslist

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.ExpectFailure.assertThat
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import com.udacity.project4.locationreminders.getOrAwaitValue
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

//todo: note, please run test cases one by one
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
@Config(sdk = intArrayOf(Build.VERSION_CODES.P))
class RemindersListViewModelTest {

    //TODO: provide testing to the RemindersListViewModel and its live data objects

    private lateinit var remindersListViewModel: RemindersListViewModel
    lateinit var reminders:MutableList<ReminderDTO>
    lateinit var reminderDataSource: FakeDataSource

    //Arrange reminder list
    val reminder1 = ReminderDTO("title1","desc","loc1",1.1,21.2)
    val reminder2 = ReminderDTO("title2","desc","loc2",2.1,22.2)
    val reminder3 = ReminderDTO("title3","desc","loc3",3.1,23.2)
    val reminder4 = ReminderDTO("title4","desc","loc4",4.1,24.2)

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel() {
        //initialize reminder list
        reminders = mutableListOf()
        reminderDataSource = FakeDataSource(reminders)
        //given a reminder list ViewModel
        remindersListViewModel =
            RemindersListViewModel(ApplicationProvider.getApplicationContext(), reminderDataSource)
    }

    @Test
    fun check_loading_reminders_given_list_of_reminders()= runBlockingTest {
        //arrange reminder list
        reminderDataSource.saveReminder(reminder1)
        reminderDataSource.saveReminder(reminder2)
        reminderDataSource.saveReminder(reminder3)
        reminderDataSource.saveReminder(reminder4)
        //when loading reminder
        remindersListViewModel.loadReminders()
        //then check reminder list size is 4
        val expected = remindersListViewModel.remindersList.getOrAwaitValue()
        assertEquals(reminderDataSource.reminders.size,expected.size)
    }

    @Test
    fun check_size_when_clear_all()= runBlockingTest {
        //when clearing list of reminder
        reminderDataSource.saveReminder(reminder1)
        reminderDataSource.deleteAllReminders()
//        val expected = remindersListViewModel.remindersList.getOrAwaitValue()
        //then empty list
        assertEquals(null, remindersListViewModel.remindersList.value)
    }
    @Test
    fun getReminder_ReturnError() = runBlockingTest{
        //given should return error is true
        reminderDataSource?.setReturnError(true)
        //when a reminder id to get reminder data
        val id = "3483"
        val res = reminderDataSource?.getReminder(id)
        //then make sure it returns error
        assertEquals(Result.Error("Test exception"), res)
    }
}