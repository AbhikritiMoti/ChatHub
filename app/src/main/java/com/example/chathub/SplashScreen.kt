package com.example.chathub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        supportActionBar?.hide()

        var shouldContinueHandler = true

        val skip = findViewById<TextView>(R.id.skip)
        skip.setOnClickListener {
            val originalColor = skip.currentTextColor
            skip.setTextColor(ContextCompat.getColor(this, R.color.purple_700))
            Handler().postDelayed({
                skip.setTextColor(originalColor)
            }, 1000) // 1 second delay

            shouldContinueHandler = false
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
        }
        Handler(Looper.getMainLooper()).postDelayed(
            {
                if (shouldContinueHandler) {
                    val i = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(i)
                    finish()
                }
            }, 3000
        )

    }
}