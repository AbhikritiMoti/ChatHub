package com.example.chathub

import android.app.*
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.RemoteViews
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.chathub.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*



class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var cld : ConnectionLiveData
    private lateinit var layout1 : ConstraintLayout
    private lateinit var layout2 : ConstraintLayout



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkNetworkConnection()
        layout1 = findViewById(R.id.layout1)
        layout2 = findViewById(R.id.layout2)



        firebaseAuth= FirebaseAuth.getInstance()

        supportActionBar?.hide()

        binding.dob.setOnClickListener{
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                this,
                { view, year, monthOfYear, dayOfMonth ->

                    val dat = (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                    binding.dob.setText(dat)
                },

                year,
                month,
                day
            )
            datePickerDialog.show()
        }

        binding.exitUp.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Exit Alert")
                .setMessage("Are you sure, you want to exit ?")
                .setCancelable(true)
                // .setMessage("this is alert")
                .setIcon(android.R.drawable.ic_dialog_alert)


            //performing positive action
            builder.setPositiveButton("Yes") { dialogInterface, which ->
                val originalColor = binding.exitUp.currentTextColor
                binding.exitUp.setTextColor(ContextCompat.getColor(this, R.color.purple_700))
                Handler().postDelayed({
                    binding.exitUp.setTextColor(originalColor)
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




        binding.registerButtonRegister.setOnClickListener {
            performRegister()

        }

        binding.alreadyHaveAccountTextView.setOnClickListener {
            val originalColor = binding.alreadyHaveAccountTextView.currentTextColor
            binding.alreadyHaveAccountTextView.setTextColor(ContextCompat.getColor(this, R.color.purple_700))
            Handler().postDelayed({
                binding.alreadyHaveAccountTextView.setTextColor(originalColor)
            }, 1000)
            Log.d("MainActivity", "Try to show login activity")

            // launch the login activity somehow
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out)


        }


        binding.selectphotoButtonRegister.setOnClickListener {
            Log.d(TAG, "Try to show photo selector")

            val getIntent = Intent(Intent.ACTION_GET_CONTENT)
            getIntent.type = "image/*"


            val pickIntent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            pickIntent.type = "image/*"

            val chooserIntent = Intent.createChooser(getIntent, "Select Image")
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))

            startActivityForResult(chooserIntent, 0)


        }
    }


    var selectedPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            // proceed and check what the selected image was....
            Log.d(TAG, "Photo was selected")

            val vg: ViewGroup? = findViewById(R.id.custom_toast)
            val inflater = layoutInflater
            val layout: View = inflater.inflate(R.layout.activity_custom_toast, vg)
            val tv = layout.findViewById<TextView>(R.id.txtVw)
            tv.text = "Uploaded"
            val toast = Toast(applicationContext)
            toast.setGravity(Gravity.BOTTOM, 0, 300)
            toast.setView(layout)
            toast.duration = Toast.LENGTH_SHORT
            toast.show()

            selectedPhotoUri = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)

            binding.selectphotoImageviewRegister.setImageBitmap(bitmap)
            binding.selectphotoButtonRegister.alpha = 0f
//            val bitmapDrawable = BitmapDrawable(bitmap)
//            binding.selectphotoButtonRegister.setBackgroundDrawable(bitmapDrawable)



        }
    }

    private fun performRegister() {
        val email = binding.emailEdittextRegister.text.toString()
        val password = binding.passwordEdittextRegister.text.toString()

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
        Log.d("MainActivity", "Email is: " + email)
        Log.d("MainActivity", "Password: $password")

        // Firebase Authentication to create a user with email and password
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener

                // else if successful
                Log.d("Main", "Successfully created user with uid:${it.result.user?.uid}")
                val vg: ViewGroup? = findViewById(R.id.custom_toast)
                val inflater = layoutInflater
                val layout: View = inflater.inflate(R.layout.activity_custom_toast, vg)
                val tv = layout.findViewById<TextView>(R.id.txtVw)
                tv.text = "SignupSuccessful\nuid:${it.result.user?.uid}"
                val toast = Toast(applicationContext)
                toast.setGravity(Gravity.BOTTOM, 0, 300)
                toast.setView(layout)
                toast.duration = Toast.LENGTH_SHORT
                toast.show()

                uploadImageToFirebaseStorage()
                val intent =Intent(this, LoginActivity::class.java)
                startActivity(intent)


            }
            .addOnFailureListener{
                Log.d("Main", "Failed to create user: ${it.message}")

                val vg: ViewGroup? = findViewById(R.id.custom_toast)
                val inflater = layoutInflater
                val layout: View = inflater.inflate(R.layout.activity_custom_toast, vg)
                val tv = layout.findViewById<TextView>(R.id.txtVw)
                tv.text = "Failed to create user: ${it.message}"
                val toast = Toast(applicationContext)
                toast.setGravity(Gravity.BOTTOM, 0, 300)
                toast.setView(layout)
                toast.duration = Toast.LENGTH_SHORT
                toast.show()
            }
    }


    private fun uploadImageToFirebaseStorage() {
        if (selectedPhotoUri == null) return

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d(TAG, "Successfully uploaded image: ${it.metadata?.path}")

                ref.downloadUrl.addOnSuccessListener {
                    Log.d(TAG, "File Location: $it")

                    saveUserToFirebaseDatabase(it.toString())
                }
            }
            .addOnFailureListener {
                Log.d(TAG, "Failed to upload image to storage: ${it.message}")
            }
    }



    private fun saveUserToFirebaseDatabase(profileImageUrl: String) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(uid, binding.usernameEdittextRegister.text.toString(),binding.emailEdittextRegister.text.toString(), profileImageUrl, binding.dob.text.toString())

        ref.setValue(user)
            .addOnSuccessListener {
                Log.d(TAG, "Finally we saved the user to Firebase Database")

//                val intent=Intent(this, LatestMessagesActivity::class.java)
//                intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
//                startActivity(intent)
            }
            .addOnFailureListener {
                Log.d(TAG, "Failed to set value to database: ${it.message}")
            }
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


}

class User(val uid: String, val name: String, val email:String, val profileImageUrl: String, val zDOB:String)