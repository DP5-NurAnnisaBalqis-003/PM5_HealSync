package com.example.cakemate

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

class UbahPassword : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ubah_password)

        mAuth = FirebaseAuth.getInstance()

        val oldPasswordInput = findViewById<EditText>(R.id.et_old_password)
        val newPasswordInput = findViewById<EditText>(R.id.et_password)
        val confirmPasswordInput = findViewById<EditText>(R.id.et_confirm_password)
        val updateButton = findViewById<Button>(R.id.btn_update_password)
        val backButton = findViewById<ImageView>(R.id.backButton)

        // Eye icons for password visibility toggle
        val ivToggleOldPassword = findViewById<ImageView>(R.id.iv_toggle_old_password)
        val ivTogglePassword = findViewById<ImageView>(R.id.iv_toggle_password)
        val ivToggleConfirmPassword = findViewById<ImageView>(R.id.iv_toggle_confirm_password)

        val sharedPreferences = getSharedPreferences("com.example.cakemate.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE)
        val registeredUsername = sharedPreferences.getString("REGISTERED_USERNAME", null)

        backButton.setOnClickListener {
            finish()
        }

        // Toggle visibility of old password
        var isOldPasswordVisible = false
        ivToggleOldPassword.setOnClickListener {
            isOldPasswordVisible = !isOldPasswordVisible
            if (isOldPasswordVisible) {
                oldPasswordInput.transformationMethod = HideReturnsTransformationMethod.getInstance()
                ivToggleOldPassword.setImageResource(R.drawable.eyehidden)
            } else {
                oldPasswordInput.transformationMethod = PasswordTransformationMethod.getInstance()
                ivToggleOldPassword.setImageResource(R.drawable.eyepw)
            }
            oldPasswordInput.setSelection(oldPasswordInput.text.length)
        }

        // Toggle visibility of new password
        var isNewPasswordVisible = false
        ivTogglePassword.setOnClickListener {
            isNewPasswordVisible = !isNewPasswordVisible
            if (isNewPasswordVisible) {
                newPasswordInput.transformationMethod = HideReturnsTransformationMethod.getInstance()
                ivTogglePassword.setImageResource(R.drawable.eyehidden)
            } else {
                newPasswordInput.transformationMethod = PasswordTransformationMethod.getInstance()
                ivTogglePassword.setImageResource(R.drawable.eyepw)
            }
            newPasswordInput.setSelection(newPasswordInput.text.length)
        }

        // Toggle visibility of confirm password
        var isConfirmPasswordVisible = false
        ivToggleConfirmPassword.setOnClickListener {
            isConfirmPasswordVisible = !isConfirmPasswordVisible
            if (isConfirmPasswordVisible) {
                confirmPasswordInput.transformationMethod = HideReturnsTransformationMethod.getInstance()
                ivToggleConfirmPassword.setImageResource(R.drawable.eyehidden)
            } else {
                confirmPasswordInput.transformationMethod = PasswordTransformationMethod.getInstance()
                ivToggleConfirmPassword.setImageResource(R.drawable.eyepw)
            }
            confirmPasswordInput.setSelection(confirmPasswordInput.text.length)
        }

        updateButton.setOnClickListener {
            val oldPassword = oldPasswordInput.text.toString().trim()
            val newPassword = newPasswordInput.text.toString().trim()
            val confirmPassword = confirmPasswordInput.text.toString().trim()

            val currentUser: FirebaseUser? = mAuth.currentUser

            if (currentUser == null) {
                Toast.makeText(this, "Pengguna tidak terautentikasi. Silakan login ulang.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            when {
                oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty() -> {
                    Toast.makeText(this, "Semua kolom harus diisi", Toast.LENGTH_SHORT).show()
                }
                newPassword == oldPassword -> {
                    Toast.makeText(this, "Kata sandi baru tidak boleh sama dengan kata sandi lama", Toast.LENGTH_SHORT).show()
                }
                newPassword != confirmPassword -> {
                    Toast.makeText(this, "Kata sandi baru dan konfirmasi harus sesuai", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    // Verifikasi kata sandi lama dan ubah kata sandi
                    currentUser.reauthenticate(EmailAuthProvider.getCredential(currentUser.email!!, oldPassword))
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                // Ubah kata sandi
                                currentUser.updatePassword(newPassword)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            // Update kata sandi di Realtime Database
                                            val database = FirebaseDatabase.getInstance("https://cakemate-5fd64-default-rtdb.firebaseio.com/")
                                            val userRef = database.getReference("users").child(currentUser.uid)
                                            userRef.child("password").setValue(newPassword)

                                            Toast.makeText(this, "Kata sandi berhasil diubah", Toast.LENGTH_SHORT).show()
                                            val intent = Intent(this, LoginActivity::class.java)
                                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                                            startActivity(intent)
                                            finish()
                                        } else {
                                            Toast.makeText(this, "Gagal mengubah kata sandi", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                            } else {
                                Toast.makeText(this, "Kata sandi lama salah", Toast.LENGTH_SHORT).show()
                            }
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Gagal melakukan autentikasi: ${it.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
        }
    }
}
