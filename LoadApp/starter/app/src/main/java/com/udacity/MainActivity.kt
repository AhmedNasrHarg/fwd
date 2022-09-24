package com.udacity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.lang.Exception

var urlName = ""
class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action
    private lateinit var radioGroup: RadioGroup
    var statusFlag = true
    var name = ""
    var statusValue = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        Toast.makeText(this,"please select the file to download",Toast.LENGTH_SHORT).show()

        radioGroup = findViewById(R.id.radio_group)
        custom_button.setOnClickListener {

            if (radioGroup.getCheckedRadioButtonId() != -1){
            try {
                if(radioGroup.getCheckedRadioButtonId() == R.id.glide){
                    urlName = "Glide"
                }
                if(radioGroup.getCheckedRadioButtonId() ==R.id.load_app){
                    urlName = "LoadApp"
                }
                if(radioGroup.getCheckedRadioButtonId() == R.id.retrofit){
                    urlName = "Retrofit"
                }
                download()
                Toast.makeText(applicationContext,radioGroup.getCheckedRadioButtonId().toString(),Toast.LENGTH_SHORT).show()

                //if come here status OK
            }catch (e:Exception){
                //if come here status failed
                statusFlag = false
            }
            createChannel(
                "chanelId","chanelName"
            )
            val notificationManager = ContextCompat.getSystemService(
                applicationContext,
                NotificationManager::class.java
            ) as NotificationManager
            //then call the extension function
            notificationManager.sendNotification(applicationContext
                .getString(R.string.notification_description), applicationContext)
            }else{
                Toast.makeText(this,"please select the file to download",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            val intent = Intent(context,DetailActivity::class.java)
            intent?.putExtra("status",statusValue)
            startActivity(intent)
        }
    }

    private fun download() {
        val request =
            DownloadManager.Request(Uri.parse(URL))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    companion object {
        private const val URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val CHANNEL_ID = "channelId"
    }
    fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context) {

        val statusIntent = Intent(applicationContext, receiver::class.java)

        if(statusFlag)
            statusValue="success"
        else
            statusValue="failed"

        statusIntent.putExtra("status",statusValue)
//        statusIntent.putExtra("url",name)

        val statusPendingIntent: PendingIntent = PendingIntent.getBroadcast(			//take care it is PI.getBroadcast not getActivity
            applicationContext,
            0,
            statusIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        //1. create builder object
        val builder = NotificationCompat.Builder( applicationContext, "chanelId")
            .addAction(R.drawable.ic_assistant_black_24dp
                ,"Check the status",
                statusPendingIntent
            )
            .setSmallIcon(R.drawable.ic_assistant_black_24dp)
            .setContentTitle(applicationContext
                .getString(R.string.notification_title))
            .setContentText(messageBody)
            .setAutoCancel(true)

        //the above is the minimum to amount of data you need to set in order to send a notification.
        //3. you need to call notify with a unique ID for your notification object from your builder.
         val NOTIFICATION_ID = 10
        notify( NOTIFICATION_ID, builder.build() )
    }
    private fun createChannel(channelId: String, channelName: String) {
        // TODO: Step 1.6 START create a channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                // TODO: Step 2.4 change importance
                NotificationManager.IMPORTANCE_HIGH
            )// TODO: Step 2.6 disable badges for this channel
                .apply {
                    setShowBadge(false)
                }

            notificationChannel.enableLights(true)
            notificationChannel.enableVibration(true)

            val notificationManager = ContextCompat.getSystemService(
                applicationContext,
                NotificationManager::class.java) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)

        }
        // TODO: Step 1.6 END create a channel
    }
}
