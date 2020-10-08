package com.rizqanmr.githubuser.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.rizqanmr.githubuser.R

class SplashScreen : Activity() {
    lateinit var handler: Handler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        handler = Handler()
        handler.postDelayed({
            val toMain = Intent(this@SplashScreen, MainActivity::class.java)
            startActivity(toMain)
            finish()
        }, 5000)
    }
}