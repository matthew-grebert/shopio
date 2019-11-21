package com.example.shopio


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class SplashActivity: AppCompatActivity() {

    lateinit var timer: Timer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        timer = Timer()
        timer.schedule(startApp(), 3000, 2000)
    }

    inner class startApp(): TimerTask() {
        override fun run() {
            runOnUiThread {

                var intent = Intent()
                intent.setClass(this@SplashActivity, ScrollingActivity::class.java)
                startActivity(intent)
                timer.cancel()
            }
        }
    }


}