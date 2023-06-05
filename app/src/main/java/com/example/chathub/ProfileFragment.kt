package com.example.chathub

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.chathub.databinding.ActivityCustomToastBinding
import com.example.chathub.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? =null
    private val binding get() = _binding!!
    val databaseRef = Firebase.database.reference
    val currentUserUid = FirebaseAuth.getInstance().currentUser!!.uid

    val user = FirebaseAuth.getInstance().currentUser
    val database = FirebaseDatabase.getInstance()
    val currentUserRef = database.getReference("users").child(user!!.uid)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {






        databaseRef.child("users/$currentUserUid/profileImageUrl").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val imageUrl = dataSnapshot.getValue(String::class.java)

                Glide.with(this@ProfileFragment)
                    .load(imageUrl)
                    .override(200, 200)
                    .into(binding.userImage)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        databaseRef.child("users/$currentUserUid/name").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val x = dataSnapshot.getValue(String::class.java)

                binding.userName.text = x
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        databaseRef.child("users/$currentUserUid/email").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val y = dataSnapshot.getValue(String::class.java)

                binding.userEmail.text = y
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        databaseRef.child("users/$currentUserUid/mob").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val y = dataSnapshot.getValue(String::class.java)
                if (!y.isNullOrEmpty()) {
                    binding.uMob.text = Editable.Factory.getInstance().newEditable(y)

                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        databaseRef.child("users/$currentUserUid/address").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val y = dataSnapshot.getValue(String::class.java)
                if (!y.isNullOrEmpty()) {
                    binding.uAddress.text = Editable.Factory.getInstance().newEditable(y)

                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        databaseRef.child("users/$currentUserUid/status").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val y = dataSnapshot.getValue(String::class.java)
                if (!y.isNullOrEmpty()) {
                    binding.uStatus.text = Editable.Factory.getInstance().newEditable(y)

                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })



//        val chatsRef = databaseRef.child("chats")
//
//
//        chatsRef.orderByChild("chats/$currentUserUid").equalTo(true).addListenerForSingleValueEvent(object: ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                // Iterate through the snapshot to retrieve the user names
//                for (chatSnapshot in snapshot.children) {
//                    val participants = chatSnapshot.child("participants")
//                    for (participantSnapshot in participants.children) {
//                        val participantUid = participantSnapshot.key
//                        if (participantUid != currentUserUid) {
//                            val participantName = participantSnapshot.child("name").value.toString()
//                            // Do something with the participant name
//                        }
//                    }
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                // Handle error
//            }
//        })
//

        _binding = FragmentProfileBinding.inflate(inflater, container, false)


        binding.updateBtn.setOnClickListener{
            var mob = binding.uMob.text.toString()
            currentUserRef.child("mob").setValue(mob)


            var address = binding.uAddress.text.toString()
            currentUserRef.child("address").setValue(address)

            var status = binding.uStatus.text.toString()
            currentUserRef.child("status").setValue(status)

            val binding = ActivityCustomToastBinding.inflate(layoutInflater)
            val tv = binding.txtVw
            tv.text = "Details Updated!"
            val toast = Toast(context)
            toast.setGravity(Gravity.BOTTOM, 0, 1163)
            toast.view = binding.root
            toast.duration = Toast.LENGTH_SHORT
            toast.show()
        }


        binding.signoutBtn.setOnClickListener{
            FirebaseAuth.getInstance().signOut()

            val binding = ActivityCustomToastBinding.inflate(layoutInflater)
            val tv = binding.txtVw
            tv.text = "Signed Out!"
            val toast = Toast(context)
            toast.setGravity(Gravity.BOTTOM, 0, 400)
            toast.view = binding.root
            toast.duration = Toast.LENGTH_SHORT
            toast.show()

            val intent = Intent(binding.root.context, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            binding.root.context.startActivity(intent)


        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}