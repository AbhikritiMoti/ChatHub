package com.example.chathub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.view.*
import android.widget.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class Rate : AppCompatActivity() {
    private lateinit var bottomNav : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rate)

        val simpleRatingBar = findViewById<RatingBar>(R.id.simpleRatingBar)
        val submitButton = findViewById<Button>(R.id.submitButton)
        val tv=findViewById<TextView>(R.id.textView)
        val feedback = findViewById<EditText>(R.id.feedback)

        val actionBar = supportActionBar

        actionBar!!.title = "Rating"
        actionBar.subtitle = "Your Fedback Matters!"

        bottomNav = findViewById(R.id.bottomNav)

        bottomNav.setSelectedItemId(R.id.rate);

        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {

                    val intent = Intent(this, Home::class.java)
                    startActivity(intent)
                    true
                }

                R.id.profile -> {
                    var url = "https://github.com/abhikritimoti/Salary-Management-System.git"
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.setDataAndType(Uri.parse(url), "text/plain")
                    val choose = Intent.createChooser(intent, "Share URL")
                    startActivity(choose)
                    true
                }
                R.id.rate -> {
                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, "https://github.com/abhikritimoti/Salary-Management-System.git")
                        type = "text/plain"
                    }

                    val shareIntent = Intent.createChooser(sendIntent, null)
                    startActivity(shareIntent)
                    true
                }
                R.id.logout -> {
                    val builder = androidx.appcompat.app.AlertDialog.Builder(this)
                    builder.setTitle("Exit Alert")
                        .setMessage("Are you sure, you want to exit ?")
                        .setCancelable(true)
                        .setIcon(android.R.drawable.ic_dialog_alert)

                    builder.setPositiveButton("Yes") { dialogInterface, which ->
                        finishAffinity()
                    }

                    builder.setNegativeButton("No") { dialogInterface, which ->
                        val vg: ViewGroup? = findViewById(R.id.custom_toast)
                        val inflater = layoutInflater
                        val layout: View = inflater.inflate(R.layout.activity_custom_toast, vg)
                        val tv = layout.findViewById<TextView>(R.id.txtVw)
                        tv.text = "clicked No"
                        val toast = Toast(applicationContext)
                        toast.setGravity(Gravity.BOTTOM, 0, 400)
                        toast.setView(layout)
                        toast.duration = Toast.LENGTH_SHORT
                        toast.show()
                    }

                    val alertDialog: androidx.appcompat.app.AlertDialog = builder.create()
                    alertDialog.show()

                    true
                }
                else -> true

            }
        }

        submitButton.setOnClickListener {
            val totalStars = "Total Stars: " + simpleRatingBar.numStars
            val rating = "Rating: " + simpleRatingBar.rating
            val fb = feedback.text.toString()

            var toast = Toast.makeText(
                this, """ $totalStars | $rating  """.trimIndent(),
                Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.TOP,1200,1200)
            toast.show()

            feedback.setText("")
            tv.setText("$rating \nWe really appreciate you taking time to share your rating with us.")

        }

    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            R.id.rate -> {
                val intent = Intent(this, Home::class.java)
                startActivity(intent)
                return true
            }
        }
        return true
    }
}