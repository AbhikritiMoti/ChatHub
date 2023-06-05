import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.chathub.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class nav_header : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nav_header)

        val databaseRef = Firebase.database.reference
        val currentUserUid = FirebaseAuth.getInstance().currentUser!!.uid
        var img = findViewById<ImageView>(R.id.uImg)

        databaseRef.child("users/$currentUserUid/profileImageUrl").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val imageUrl = dataSnapshot.getValue(String::class.java)
                Glide.with(this@nav_header)
                    .load(imageUrl)
                    .override(200, 200)
                    .into(img)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })


    }
}