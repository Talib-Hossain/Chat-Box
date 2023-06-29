package com.example.chatbox

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class UserAdapter(val context: Context, val userList: ArrayList<User>):
    RecyclerView.Adapter<UserAdapter.UserViewHolder> (){

    //val DEFAULT_URL: String= "https://firebasestorage.googleapis.com/v0/b/chat-box-8efe2.appspot.com/o/images%2Fprofile_image.png?alt=media&token=dae2fd64-be76-43af-8e08-eb86ab2567b4"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.user_layout, parent, false)
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser= userList[position]
        holder.textName.text= currentUser.name

        if(currentUser.profileImageUrl==null){
            Log.d("UserAdapter","Entered in the if check")
            holder.profileImage.setImageResource(R.drawable.profile_image)
        }else{
            Picasso.get().load(currentUser.profileImageUrl).into(holder.profileImage)
        }

        holder.itemView.setOnClickListener {
            val intent= Intent(context, ChatActivity::class.java)

            intent.putExtra("name", currentUser.name)
            intent.putExtra("uid", currentUser.uid)

            context.startActivity(intent)
        }
    }

    class UserViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val textName = itemView.findViewById<TextView>(R.id.txtName)
        val profileImage= itemView.findViewById<CircleImageView>(R.id.userLayoutProfileImage)
    }
}