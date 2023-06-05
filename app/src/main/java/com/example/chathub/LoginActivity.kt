package com.example.chathub

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.RemoteViews
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.chathub.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private lateinit var cld : ConnectionLiveData

    private lateinit var layout1 : ConstraintLayout
    private lateinit var layout2 : ConstraintLayout

    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder: Notification.Builder
    private val channelId = "i.apps.notifications"
    private val description = "Test notification"
    @RequiresApi(Build.VERSION_CODES.M)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        supportActionBar?.hide()

        binding.exitIn.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Exit Alert")
                .setMessage("Are you sure, you want to exit ?")
                .setCancelable(true)
                // .setMessage("this is alert")
                .setIcon(android.R.drawable.ic_dialog_alert)


            //performing positive action
            builder.setPositiveButton("Yes") { dialogInterface, which ->
                    val originalColor = binding.exitIn.currentTextColor
                    binding.exitIn.setTextColor(ContextCompat.getColor(this, R.color.purple_700))
                    Handler().postDelayed({
                        binding.exitIn.setTextColor(originalColor)
                    }, 1000) // 1 second delay
                    finishAffinity()

            }
            //performing cancel action
            builder.setNeutralButton("Cancel") { dialogInterface, which ->
                val vg: ViewGroup? = findViewById(R.id.custom_toast)
                val inflater = layoutInflater
                val layout: View = inflater.inflate(R.layout.activity_custom_toast, vg)
                val tv = layout.findViewById<TextView>(R.id.txtVw)
                tv.text = "Operation Aborted"
                val toast = Toast(applicationContext)
                toast.setGravity(Gravity.BOTTOM, 0, 300)
                toast.setView(layout)
                toast.duration = Toast.LENGTH_SHORT
                toast.show()
            }
            //performing negative action
            builder.setNegativeButton("No") { dialogInterface, which ->
                val vg: ViewGroup? = findViewById(R.id.custom_toast)
                val inflater = layoutInflater
                val layout: View = inflater.inflate(R.layout.activity_custom_toast, vg)
                val tv = layout.findViewById<TextView>(R.id.txtVw)
                tv.text = "Welcome Back"
                val toast = Toast(applicationContext)
                toast.setGravity(Gravity.BOTTOM, 0, 300)
                toast.setView(layout)
                toast.duration = Toast.LENGTH_SHORT
                toast.show()
            }

            val alertDialog: AlertDialog = builder.create()
            alertDialog.show()

        }

        binding.loginButtonLogin.setOnClickListener {
            performLogin()
        }

        binding.backToRegisterTextview.setOnClickListener{
            val originalColor = binding.backToRegisterTextview.currentTextColor
            binding.backToRegisterTextview.setTextColor(ContextCompat.getColor(this, R.color.purple_700))
            Handler().postDelayed({
                binding.backToRegisterTextview.setTextColor(originalColor)
                                  }, 1000) // 1 second delay

            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out)
        }

        checkNetworkConnection()

        layout1 = findViewById(R.id.layout1)
        layout2 = findViewById(R.id.layout2)


    }

    private fun checkNetworkConnection() {
        cld = ConnectionLiveData(application)

        cld.observe(this, { isConnected ->

            if (isConnected){

                layout1.visibility = View.VISIBLE
                layout2.visibility = View.GONE

            }else{
                layout1.visibility = View.GONE
                layout2.visibility = View.VISIBLE
            }

        })
    }



    private fun performLogin() {
        val email = binding.emailEdittextLogin.text.toString()
        val password = binding.passwordEdittextLogin.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            val vg: ViewGroup? = findViewById(R.id.custom_toast)
            val inflater = layoutInflater
            val layout: View = inflater.inflate(R.layout.activity_custom_toast, vg)
            val tv = layout.findViewById<TextView>(R.id.txtVw)
            tv.text = "Empty Fields"
            val toast = Toast(applicationContext)
            toast.setGravity(Gravity.BOTTOM, 0, 300)
            toast.setView(layout)
            toast.duration = Toast.LENGTH_SHORT
            toast.show()
            return
        }



        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener


                //else if successful
                Log.d("Login", "Successfully logged in: ${it.result.user?.uid}")

                val vg: ViewGroup? = findViewById(R.id.custom_toast)
                val inflater = layoutInflater
                val layout: View = inflater.inflate(R.layout.activity_custom_toast, vg)
                val tv = layout.findViewById<TextView>(R.id.txtVw)
                tv.text = "Login Success"
                val toast = Toast(applicationContext)
                toast.setGravity(Gravity.BOTTOM, 0, 300)
                toast.setView(layout)
                toast.duration = Toast.LENGTH_SHORT
                toast.show()

                val intent = Intent(this, Home::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                overridePendingTransition(androidx.appcompat.R.anim.abc_slide_in_top,
                    androidx.appcompat.R.anim.abc_slide_in_top)

                //notify
                val contentView = RemoteViews(packageName, R.layout.activity_after_notification)
                val notificationMessage = "You have successfully LoggedIn"
                contentView.setTextViewText(R.id.textView, notificationMessage)
                contentView.setImageViewResource(R.id.notification_icon, R.drawable.logo2)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    notificationChannel = NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
                    notificationChannel.enableLights(true)
                    notificationChannel.lightColor = Color.GREEN
                    notificationChannel.enableVibration(false)
                    notificationManager.createNotificationChannel(notificationChannel)
                    builder = Notification.Builder(this, channelId)
                        .setContent(contentView)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.ic_launcher_background))
                } else {
                    builder = Notification.Builder(this)
                        .setContent(contentView)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.ic_launcher_background))
                }

                notificationManager.notify(1234, builder.build())
            }
            .addOnFailureListener {

                val vg: ViewGroup? = findViewById(R.id.custom_toast)
                val inflater = layoutInflater
                val layout: View = inflater.inflate(R.layout.activity_custom_toast, vg)
                val tv = layout.findViewById<TextView>(R.id.txtVw)
                tv.text = "Failed to log in: ${it.message}"
                val toast = Toast(applicationContext)
                toast.setGravity(Gravity.BOTTOM, 0, 300)
                toast.setView(layout)
                toast.duration = Toast.LENGTH_SHORT
                toast.show()
            }
    }
}