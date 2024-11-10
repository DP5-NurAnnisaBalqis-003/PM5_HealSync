package com.lab5.myapplication

import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class MyActivity : AppCompatActivity() {
    private lateinit var imgProfile: ImageView

    // Register the launcher for selecting an image from the gallery
    private val selectImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            imgProfile.setImageURI(uri)
        } else {
            // Handle case where no image was selected
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my)

        imgProfile = findViewById(R.id.img_profile)

        // Set click listener to launch image picker directly without checking permissions
        imgProfile.setOnClickListener {
            changeProfilePicture()
        }
    }

    // Method to launch image selection
    private fun changeProfilePicture() {
        selectImageLauncher.launch("image/*")
    }
}
