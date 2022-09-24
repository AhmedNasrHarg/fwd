package com.udacity

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    lateinit var name:TextView
    lateinit var status:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        name = findViewById(R.id.nameTextView)
        status = findViewById(R.id.statusTextView)

        val intnt = intent
        status.text = intnt.extras?.get("status") as String
        name.text = urlName
//        Toast.makeText(applicationContext,name.text,Toast.LENGTH_SHORT).show()

    }

}
