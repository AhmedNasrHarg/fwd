package com.udacity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity

class DownloadReceiver: BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        val intent = Intent(p0,DetailActivity::class.java)
        intent?.putExtra("status",p1?.extras?.get("status") as String)
        p0?.startActivity(intent)
    }
}