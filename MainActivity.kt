package com.example.cakemate

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cakemate.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.security.MessageDigest

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var isPasswordVisible = false
    private var isConfirmPasswordVisible = false
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance() // Inisialisasi FirebaseAuth

        // Fokus pada username di awal
        binding.etUsername.requestFocus()

        // Toggle visibilitas password
        binding.ivToggleEtPassword.setOnClickListener {
            togglePasswordVisibility(
                binding.etPassword,
                binding.ivToggleEtPassword,
                isPasswordVisible
            ) { isPasswordVisible = it }
        }

        // Toggle visibilitas konfirmasi password
        binding.ivToggleConfirmPassword.setOnClickListener {
            togglePasswordVisibility(
                binding.etConfirmPassword,
                binding.ivToggleConfirmPassword,
                isConfirmPasswordVisible
            ) { isConfirmPasswordVisible = it }
        }

        // Tombol daftar
        binding.btnRegister.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val confirmPassword = binding.etConfirmPassword.text.toString().trim()

            val usernameRegex = "^[a-zA-Z0-9 ]{5,35}$".toRegex()

            when {
                username.isEmpty() -> {
                    Toast.makeText(this, "Masukkan Nama Pengguna Terlebih Dahulu", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                !usernameRegex.matches(username) -> {
                    Toast.makeText(
                        this,
                        "Nama Pengguna Harus Terdiri Dari 5-35 Huruf atau Angka",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
                password.isEmpty() -> {
                    Toast.makeText(this, "Masukkan Kata Sandi Terlebih Dahulu", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                password != confirmPassword -> {
                    Toast.makeText(this, "Kata Sandi dan Konfirmasi Kata Sandi Harus Sesuai", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            // Firebase Authentication untuk registrasi
            auth.createUserWithEmailAndPassword("$username@cakemate.com", password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Dapatkan UID dari Firebase Authentication
                        val uid = task.result?.user?.uid ?: return@addOnCompleteListener
                        saveUserToDatabase(uid, username, password)
                    } else {
                        Toast.makeText(this, "Registrasi Gagal: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        // Pindah ke halaman login
        binding.tvLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun saveUserToDatabase(uid: String, username: String, password: String) {
        val database = FirebaseDatabase.getInstance("https://cakemate-5fd64-default-rtdb.firebaseio.com/")
        val userRef = database.getReference("users").child(uid)

        val user = mapOf(
            "username" to username,
            "password" to hashPassword(password) // Simpan password dalam bentuk hash
        )

        userRef.setValue(user)
            .addOnSuccessListener {
                Toast.makeText(this, "Registrasi Berhasil, Silahkan Login Terlebih Dahulu $username", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Gagal menyimpan data: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun togglePasswordVisibility(editText: EditText, imageView: ImageView, isVisible: Boolean, setVisible: (Boolean) -> Unit) {
        val newVisibility = !isVisible
        setVisible(newVisibility)
        if (newVisibility) {
            editText.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            imageView.setImageResource(R.drawable.eyepw)
        } else {
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            imageView.setImageResource(R.drawable.eyehidden)
        }
        editText.setSelection(editText.text.length)
    }

    private fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(password.toByteArray())
        return hashBytes.joinToString("") { "%02x".format(it) }
    }
}
