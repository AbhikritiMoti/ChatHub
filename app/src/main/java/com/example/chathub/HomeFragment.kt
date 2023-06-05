package com.example.chathub

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chathub.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class HomeFragment : Fragment() {

    private lateinit var binding: ActivityHomeBinding

    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userList: ArrayList<Users>
    private lateinit var adapter: UserAdapter

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

    lateinit var v: View




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        v = inflater.inflate(R.layout.fragment_home, container, false)

        val con = v.context
        mAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().getReference()

        userList = ArrayList()
        adapter = UserAdapter(con,userList)

        userRecyclerView = v.findViewById(R.id.userRecyclerView)

        userRecyclerView.layoutManager = LinearLayoutManager(con)
        userRecyclerView.adapter = adapter

        mDbRef.child("users").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                userList.clear()
                for (posSnapshot in snapshot.children){

                    val currentUser = posSnapshot.getValue(Users::class.java)

                    if(mAuth.currentUser?.uid !=currentUser?.uid){
                        userList.add(currentUser!!)
                    }

                }



                adapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
        return v
    }

}