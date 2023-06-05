package com.example.chathub

import android.os.Bundle
import android.text.Editable
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.chathub.databinding.FragmentReateBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class ReateFragment : Fragment() {
    private var _binding: FragmentReateBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentReateBinding.inflate(inflater, container, false)

        val user = FirebaseAuth.getInstance().currentUser
        val database = FirebaseDatabase.getInstance()
        val currentUserRef = database.getReference("users").child(user!!.uid)

        val databaseRef = Firebase.database.reference
        val currentUserUid = FirebaseAuth.getInstance().currentUser!!.uid




//        val actionBar = supportActionBar
//
//        actionBar!!.title = "Rating"
//        actionBar.subtitle = "Your Fedback Matters!"
        binding.submitButton.setOnClickListener {
            val totalStars = "Total Stars: " + binding.simpleRatingBar.numStars
            val rating = "Rating: " + binding.simpleRatingBar.rating
            val fb = binding.feedback.text.toString()


            var toast = Toast.makeText(
                requireContext(), """ $totalStars | $rating  """.trimIndent(),
                Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.TOP,1200,1200)
            toast.show()


            binding.textView.setText("$rating \nWe really appreciate you taking time to share your rating with us.")

            val data = binding.feedback.text.toString()
            currentUserRef.child("feedback").setValue(data)
            binding.feedback.setText("")

        }


        databaseRef.child("users/$currentUserUid/feedback").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val y = dataSnapshot.getValue(String::class.java)
                if (!y.isNullOrEmpty()) {
                    binding.recentH.text = Editable.Factory.getInstance().newEditable(y)

                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })


        return return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}