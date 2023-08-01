package com.example.chatbox

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView
import java.util.UUID

class SignUp : AppCompatActivity() {

    companion object {
        val TAG = "SignUp"
    }

    val basicURL =
        "https://firebasestorage.googleapis.com/v0/b/chat-box-8efe2.appspot.com/o/images%2Fprofile_image.png?alt=media&token=dae2fd64-be76-43af-8e08-eb86ab2567b4"
    private lateinit var edtName: EditText
    private lateinit var etEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnSignUp: Button
    private lateinit var profileImage: CircleImageView

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        supportActionBar?.hide()
        mAuth = FirebaseAuth.getInstance()

        etEmail = findViewById(R.id.etEmail)
        edtName = findViewById(R.id.etName)
        edtPassword = findViewById(R.id.etPassword)
        btnSignUp = findViewById(R.id.btnSignUp)
        profileImage = findViewById(R.id.profileImage)

        profileImage.setOnClickListener {

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

        btnSignUp.setOnClickListener {
            val name = edtName.text.toString()
            val email = etEmail.text.toString()
            val password = edtPassword.text.toString()

            signUp(name, email, password)
        }
    }

    var selectedPhotoUri: Uri? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            // proceed and check what the selected image was....
            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
            profileImage.setImageBitmap(bitmap)
        }
    }

    private fun signUp(name: String, email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                //var profileImageUrl = uploadImageToFirebaseStorage()

                uploadImageToFirebaseStorage { profileImageUrl ->
                    if (profileImageUrl != null) {
                        Log.d("Talib", "In the if part of ADD user profile url: $profileImageUrl")
                        addUserToDatabase(
                            name, email, mAuth.currentUser?.uid!!, profileImageUrl
                        )
                    }
                }
                val intent = Intent(this@SignUp, MainActivity::class.java)
                finish()
                startActivity(intent)
            } else {
                Toast.makeText(this@SignUp, "Error Occured", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadImageToFirebaseStorage(callback: (String?) -> Unit) {
        var profileImageUrl: String = null.toString()
        //if (selectedPhotoUri == null) return
        if (selectedPhotoUri == null) {
            // If no profile image is selected, use the default URL
            callback(basicURL)
            return
        }
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(selectedPhotoUri!!).addOnSuccessListener {
            Log.d(TAG, "Successfully uploaded image: ${it.metadata?.path}")
            ref.downloadUrl.addOnSuccessListener {
                profileImageUrl = it.toString()
                callback(profileImageUrl)
                Log.d(TAG, "Checking URL after storing in profileImageUrl: $profileImageUrl")
                Toast.makeText(this, "Image Upload Successful", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "File Location: $it")
            }.addOnFailureListener {
                callback(null)
                Log.d(TAG, "Failed to store URL")
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Image Upload failed", Toast.LENGTH_SHORT).show()
            Log.d(TAG, "Failed to upload image to storage: ${it.message}")
        }
        Log.d(TAG, "Checking URL before return: $profileImageUrl")
    }

    private fun getProfileImageUrl(profileImageUrl: String): String {
        return profileImageUrl
    }

    private fun addUserToDatabase(
        name: String, email: String, uid: String, profileImageUrl: String
    ) {
        Log.d(TAG, "Checking URL at AddDB Fun $profileImageUrl")
        mDbRef = FirebaseDatabase.getInstance().getReference()
        mDbRef.child("user").child(uid).setValue(User(name, email, uid, profileImageUrl))
    }
}