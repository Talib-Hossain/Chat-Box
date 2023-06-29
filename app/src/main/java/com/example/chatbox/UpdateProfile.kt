package com.example.chatbox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class UpdateProfile : AppCompatActivity() {

    private lateinit var profileImageView: CircleImageView
    private lateinit var selectProfilePicture: Button
    private lateinit var updateProfilePicture: Button
    private lateinit var currentUserUid:String

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profile)

        profileImageView=findViewById(R.id.profileImageUpdateProfile)
        selectProfilePicture=findViewById(R.id.selectProfilePictureButton)
        updateProfilePicture= findViewById(R.id.updateProfilePictureButton)

        mAuth= FirebaseAuth.getInstance()
        mDbRef= FirebaseDatabase.getInstance().getReference()
        currentUserUid= mAuth.currentUser?.uid.toString()

        //value = DataSnapshot item = mDbRef . child ("user").child()

        mDbRef.child("user").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                for (postSnapshot in snapshot.children){
                    val currentUser= postSnapshot.getValue(User::class.java)
                    if(currentUser?.profileImageUrl!=null){
                        Log.d("Update Profile","Enter in the Update Profile If check, URL: ${currentUser.profileImageUrl}")
                        Picasso.get().load(currentUser.profileImageUrl).placeholder(R.drawable.profile_image).into(profileImageView)
                    }
//                    else{
//                        profileImageView.setImageResource(R.drawable.profile_image)
//                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


    }
}