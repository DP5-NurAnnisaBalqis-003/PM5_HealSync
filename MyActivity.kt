package com.example.cakemate
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.io.ByteArrayOutputStream
import android.util.Base64

class MyActivity : AppCompatActivity() {

    private lateinit var imgProfile: ImageView
    private lateinit var btnUbah: MaterialButton
    private var selectedImageUri: Uri? = null
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseFirestore = FirebaseFirestore.getInstance()


    // Launcher for picking an image
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

        // Set up the back button functionality
        val backButton = findViewById<ImageView>(R.id.backButton)
        backButton.setOnClickListener {
            // Finish the current activity and go back to the previous one
            onBackPressed()
        }

        // Set up button to save changes
        btnUbah.setOnClickListener {
            if (selectedImageUri != null) {
                // Convert image to Base64 string
                val imageBase64 = convertImageToBase64(selectedImageUri!!)
                // Save to Firestore
                saveProfileToFirestore(imageBase64)
            } else {
                Toast.makeText(this, "Pilih Gambar Terlebih Dahulu", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Launch the image picker
    private fun changeProfilePicture() {
        selectImageLauncher.launch("image/*")
    }

    // Convert image URI to Base64 string
    private fun convertImageToBase64(uri: Uri): String {
        val inputStream = contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    // Save the Base64 string to Firestore
    private fun saveProfileToFirestore(imageBase64: String) {
        val userId = firebaseAuth.currentUser?.uid
        if (userId != null) {
            val userRef = firebaseFirestore.collection("users").document(userId)
            userRef.set(
                hashMapOf(
                    "profilePicture" to imageBase64 // Save the image as Base64
                )
            ).addOnSuccessListener {
                Toast.makeText(this, "Foto Profil berhasil diubah", Toast.LENGTH_SHORT).show()
                // Send the result back to the calling activity if needed
                val resultIntent = Intent().apply {
                    putExtra("imageBase64", imageBase64)
                }
                setResult(RESULT_OK, resultIntent)
                finish()
            }.addOnFailureListener { exception ->
                Toast.makeText(this, "Gagal menyimpan foto: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Retrieve the profile picture from Firestore
    override fun onStart() {
        super.onStart()
        val userId = firebaseAuth.currentUser?.uid
        if (userId != null) {
            val userRef = firebaseFirestore.collection("users").document(userId)
            userRef.get().addOnSuccessListener { document ->
                if (document.exists()) {
                    val profilePictureBase64 = document.getString("profilePicture")
                    if (profilePictureBase64 != null) {
                        val decodedBytes = Base64.decode(profilePictureBase64, Base64.DEFAULT)
                        val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                        imgProfile.setImageBitmap(bitmap)
                    }
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(this, "Gagal mengambil data profil: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
