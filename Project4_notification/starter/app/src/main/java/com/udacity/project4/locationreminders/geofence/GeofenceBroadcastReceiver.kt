package com.udacity.project4.locationreminders.geofence

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import com.udacity.project4.R
import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem
import com.udacity.project4.locationreminders.savereminder.ACTION_GEOFENCE_EVENT
import com.udacity.project4.utils.sendNotification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import kotlin.coroutines.CoroutineContext

/**
 * Triggered by the Geofence.  Since we can have many Geofences at once, we pull the request
 * ID from the first Geofence, and locate it within the cached data in our Room DB
 *
 * Or users can add the reminders and then close the app, So our app has to run in the background
 * and handle the geofencing in the background.
 * To do that you can use https://developer.android.com/reference/android/support/v4/app/JobIntentService to do that.
 *
 */

class GeofenceBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        //TODO: implement the onReceive method to receive the geofencing events at the background.      y3nee call job intent service
        GeofenceTransitionsJobIntentService.enqueueWork(context,intent)         //new, t2ribn we're DONE with this line only

        //todo: remove all below b2aaa                                          //old
            /*if (intent.action == ACTION_GEOFENCE_EVENT) {
                val curReminer:ReminderDataItem = intent.extras?.get("geofence") as ReminderDataItem
                val geofencingEvent = GeofencingEvent.fromIntent(intent)
                if (geofencingEvent.hasError()) {
                    return
                }

                if (geofencingEvent.geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
                    Log.v("TAG", context.getString(R.string.geofence_entered))
                    val fenceId = when {
                        geofencingEvent.triggeringGeofences.isNotEmpty() ->
                            geofencingEvent.triggeringGeofences[0].requestId
                        else -> {
                            Log.e("TAG", "No Geofence Trigger Found! Abort mission!")
                            return
                        }
                    }
                    sendNotification(context, curReminer)
                }
            }*/
    }
}