package com.lab5.myapplication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class MyActivity : AppCompatActivity() {
    private lateinit var imgProfile: ImageView
    private lateinit var btnUbah: MaterialButton
    private var selectedImageUri: Uri? = null

    private val selectImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            selectedImageUri = uri
            imgProfile.setImageURI(uri)
            Toast.makeText(this, "Image selected", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my)

        imgProfile = findViewById(R.id.img_profile)
        btnUbah = findViewById(R.id.btn_ubah)

        imgProfile.setOnClickListener {
            changeProfilePicture()
        }

        btnUbah.setOnClickListener {
            if (selectedImageUri != null) {
                val resultIntent = Intent().apply {
                    putExtra("imageUri", selectedImageUri.toString())
                }
                setResult(RESULT_OK, resultIntent)
                finish()
            } else {
                Toast.makeText(this, "Please select an image first", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun changeProfilePicture() {
        selectImageLauncher.launch("image/*")
    }
}
