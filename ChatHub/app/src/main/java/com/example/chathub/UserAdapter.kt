package com.example.chathub

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chathub.databinding.UserLayoutBinding
import java.io.ByteArrayOutputStream

class UserAdapter(val context: Context, val userList:ArrayList<Users>):
    RecyclerView.Adapter<UserAdapter.UserViewHolder>(){

    inner class UserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var binding: UserLayoutBinding = UserLayoutBinding.bind(itemView)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {

        val view: View= LayoutInflater.from(context).inflate(R.layout.user_layout,parent,false)
        return UserViewHolder(view)
    }


    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {

        val currentUser = userList[position]

        Glide.with(context).load(currentUser.profileImageUrl).into(holder.binding.userImage)

        holder.binding.txtName.text = currentUser.name
        holder.binding.id.text = currentUser.email

        holder.itemView.setOnClickListener{
            val intent = Intent(context,ChatActivity::class.java)

            intent.putExtra("name",currentUser.name)
            intent.putExtra("uid",currentUser.uid)
            intent.putExtra("email",currentUser.uid)

            val bitmap = (holder.binding.userImage.drawable as BitmapDrawable).bitmap
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray = stream.toByteArray()

            intent.putExtra("userImage", byteArray)

            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return userList.size

    }




}