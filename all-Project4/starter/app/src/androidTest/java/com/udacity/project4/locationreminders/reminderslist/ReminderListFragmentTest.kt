package com.udacity.project4.locationreminders.reminderslist

import android.app.PendingIntent.getActivity
import android.app.usage.UsageEvents
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.service.autofill.Validators.not
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.R
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import com.udacity.project4.locationreminders.data.local.RemindersLocalRepositoryTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import net.bytebuddy.matcher.ElementMatchers.`is`
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.robolectric.annotation.Config
import java.util.regex.Pattern.matches

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
//UI Testing
@MediumTest
@Config(sdk = intArrayOf(Build.VERSION_CODES.P))
class ReminderListFragmentTest {

//    TODO: test the navigation of the fragments.
    var repo: RemindersLocalRepositoryTest? = null
    lateinit var reminders : MutableList<ReminderDTO>
    @Before
    fun prepareRepository(){
        reminders = mutableListOf()
        repo = RemindersLocalRepositoryTest(reminders)
    }
    @Test
    fun clickAddReminerFab_navigateToSaveReminder() = runBlockingTest {
        // GIVEN - On the ReminderListFragment
        val scenario = launchFragmentInContainer<ReminderListFragment>(Bundle(), R.style.AppTheme)
        val navController = mock(NavController::class.java)

        // WHEN - Click on the add reminder fab
        onView(withId(R.id.addReminderFAB))
            .perform( click())
        // THEN - Verify that we navigate to the add reminder fragment
        verify(navController).navigate(
            ReminderListFragmentDirections.toSaveReminder()
            )
    }
    @Test
    fun clickAddReminerFab_navigateToSaveReminderAndShowToast(){
        // GIVEN - On the ReminderListFragment
        val scenario = launchFragmentInContainer<ReminderListFragment>(Bundle(), R.style.AppTheme)
        val navController = mock(NavController::class.java)

        // WHEN - Click on the add reminder fab
        onView(withId(R.id.addReminderFAB))
            .perform( click())
        // THEN - Verify that we navigate to the add reminder fragment
        verify(navController).navigate(
            ReminderListFragmentDirections.toSaveReminder()
        )
        //check toast appears
        TODO("test toast here b2aaaaa")

    }
    @After
    fun cleanRepository()= runBlockingTest{
        repo = null
    }
//    TODO: test the displayed data on the UI.
    @Test
    fun addedNewReminder_DisplayedInUi() = runBlockingTest{
        //given save a reminder to db
        val reminder1 = ReminderDTO("title1","desc1","loc1",1.1,1.1)
        repo?.saveReminder(reminder1)
        //when reminder list fragment launched to laod saved reminder
        launchFragmentInContainer<ReminderListFragment>(Bundle(), R.style.AppTheme)
        //then make sure reminder details are displayed on UI
        onView(withId(R.id.reminderssRecyclerView))
            .perform(
                RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                hasDescendant(withText("title1")), click()))

    }
//    TODO: add testing for the error messages.
    @Test
    fun getReminder_ReturnError() = runBlockingTest{
        //given should return error is true
        repo?.setReturnError(true)
        //when a reminder id to get reminder data
        val id = "3483"
        val res = repo?.getReminder(id)
        //then make sure it returns error
        assertEquals(Result.Error("Test exception"), res)
    }
}