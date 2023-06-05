package com.example.chathub

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chathub.databinding.ActivityCustomToastBinding
import com.example.chathub.databinding.ActivityDrawerBinding
import com.example.chathub.databinding.ActivityHomeBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Home : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding


    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);


        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.homeF, HomeFragment())
                .commit()
        }

        navView.setNavigationItemSelectedListener {menuItem ->
            val actionView = LayoutInflater.from(this).inflate(R.layout.custom_navigation_item, null)
            val textView = actionView.findViewById<TextView>(R.id.nav_item_text)
            textView.text = menuItem.title
            menuItem.actionView = actionView
            true
            when (menuItem.itemId) {
                R.id.home -> supportFragmentManager.beginTransaction()
                    .replace(R.id.homeF, HomeFragment())
                    .commit()

                R.id.profile -> supportFragmentManager.beginTransaction()
                    .replace(R.id.homeF, ProfileFragment())
                    .commit()

                R.id.rate -> supportFragmentManager.beginTransaction()
                    .replace(R.id.homeF, ReateFragment())
                    .commit()

                R.id.git -> {
                    var url = "https://github.com/abhikritimoti"
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.setDataAndType(Uri.parse(url), "text/plain")
                    val choose = Intent.createChooser(intent, "Share URL")
                    startActivity(choose)
                    true
                }

                R.id.share -> {
                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, "https://github.com/abhikritimoti/")
                        type = "text/plain"
                    }

                    val shareIntent = Intent.createChooser(sendIntent, null)
                    startActivity(shareIntent)
                    true
                }

                R.id.newuser -> {
                    FirebaseAuth.getInstance().signOut()
                    //toast
                    val vg: ViewGroup? = findViewById(R.id.custom_toast)
                    val inflater = layoutInflater
                    val layout: View = inflater.inflate(R.layout.activity_custom_toast, vg)
                    val tv = layout.findViewById<TextView>(R.id.txtVw)
                    tv.text = "Register here"
                    val toast = Toast(applicationContext)
                    toast.setGravity(Gravity.BOTTOM, 0, 300)
                    toast.setView(layout)
                    toast.duration = Toast.LENGTH_SHORT
                    toast.show()

                    val intent = Intent(binding.root.context, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    binding.root.context.startActivity(intent)
                }

                R.id.signout -> {
                    FirebaseAuth.getInstance().signOut()
                    //toast
                    val vg: ViewGroup? = findViewById(R.id.custom_toast)
                    val inflater = layoutInflater
                    val layout: View = inflater.inflate(R.layout.activity_custom_toast, vg)
                    val tv = layout.findViewById<TextView>(R.id.txtVw)
                    tv.text = "Signed Out"
                    val toast = Toast(applicationContext)
                    toast.setGravity(Gravity.BOTTOM, 0, 300)
                    toast.setView(layout)
                    toast.duration = Toast.LENGTH_SHORT
                    toast.show()

                    val intent = Intent(binding.root.context, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    binding.root.context.startActivity(intent)

                }

                R.id.exit -> finishAffinity()


            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true

        }


        val actionBar = supportActionBar
//        actionBar!!.title = "  ChatHub"
//        actionBar.subtitle = "   Design a custom Action Bar"
        actionBar?.setIcon(R.drawable.logo2)

        actionBar?.setDisplayUseLogoEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.share -> {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "https://github.com/abhikritimoti")
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
                true
            }
            R.id.logout -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("SignOut Alert")
                    .setMessage("Are you sure, you want to continue ?")
                    .setCancelable(true)
                    // .setMessage("this is alert")
                    .setIcon(android.R.drawable.ic_dialog_alert)


                //performing positive action
                builder.setPositiveButton("Yes") { dialogInterface, which ->
                    FirebaseAuth.getInstance().signOut()


                    //toast
                    val vg: ViewGroup? = findViewById(R.id.custom_toast)
                    val inflater = layoutInflater
                    val layout: View = inflater.inflate(R.layout.activity_custom_toast, vg)
                    val tv = layout.findViewById<TextView>(R.id.txtVw)
                    tv.text = "Signed Out"
                    val toast = Toast(applicationContext)
                    toast.setGravity(Gravity.BOTTOM, 0, 300)
                    toast.setView(layout)
                    toast.duration = Toast.LENGTH_SHORT
                    toast.show()

                    val intent = Intent(binding.root.context, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    binding.root.context.startActivity(intent)
                }
                //performing cancel action
                builder.setNeutralButton("Cancel") { dialogInterface, which ->
                    //toast
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
                    //toast
                    val vg: ViewGroup? = findViewById(R.id.custom_toast)
                    val inflater = layoutInflater
                    val layout: View = inflater.inflate(R.layout.activity_custom_toast, vg)
                    val tv = layout.findViewById<TextView>(R.id.txtVw)
                    tv.text = "Not SignedOut"
                    val toast = Toast(applicationContext)
                    toast.setGravity(Gravity.BOTTOM, 0, 300)
                    toast.setView(layout)
                    toast.duration = Toast.LENGTH_SHORT
                    toast.show()
                }

                val alertDialog: AlertDialog = builder.create()
                alertDialog.show()
            }
        }

        if (toggle.onOptionsItemSelected(item)) {

            return true
        }
        return true
    }
}