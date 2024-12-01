package com.example.cakemate

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
            Toast.makeText(this, "Berhasil Pilih Gambar, Silahkan Tekan Tombol Ubah", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Tidak Ada Gambar Yang Dipilih", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my)

        imgProfile = findViewById(R.id.img_profile)
        btnUbah = findViewById(R.id.btn_ubah)

        // Set up profile image change functionality
        imgProfile.setOnClickListener {
            changeProfilePicture()
        }

        // Set up button to save changes
        btnUbah.setOnClickListener {
            if (selectedImageUri != null) {
                Toast.makeText(this, "Foto Profil berhasil diubah", Toast.LENGTH_SHORT).show() // Tambahkan pemberitahuan di sini
                val resultIntent = Intent().apply {
                    putExtra("imageUri", selectedImageUri.toString())
                }
                setResult(RESULT_OK, resultIntent)
                finish()
            } else {
                Toast.makeText(this, "Pilih Gambar Terlebih Dahulu", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun changeProfilePicture() {
        selectImageLauncher.launch("image/*")
    }
}
