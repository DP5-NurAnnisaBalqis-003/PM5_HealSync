package com.lab5.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lab5.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val sharedPrefFile = "com.lab5.myapplication.PREFERENCE_FILE_KEY"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Automatically show the keyboard for the username field
        binding.etUsername.requestFocus()
        showKeyboard(binding.etUsername)

        val sharedPreferences = getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)

        binding.btnRegister.setOnClickListener {
            val email = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()

            // Regular expression to check for only alphanumeric characters and length constraints
            val alphanumericRegex = "^[a-zA-Z0-9]{5,15}$".toRegex()

            when {
                email.isEmpty() -> {
                    Toast.makeText(this, "Masukkan Email Terlebih Dahulu", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener // Stop if email is empty
                }
                !alphanumericRegex.matches(email) -> {
                    Toast.makeText(this, "Nama Pengguna Harus Terdiri Dari 5-15 Huruf atau Angka", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener // Stop if username is invalid
                }
                password.isEmpty() -> {
                    Toast.makeText(this, "Masukkan Kata Sandi Terlebih Dahulu", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener // Stop if password is empty
                }
                !alphanumericRegex.matches(password) -> {
                    Toast.makeText(this, "Nama Pengguna Harus Terdiri Dari 5-15 Huruf atau Angka", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener // Stop if password is invalid
                }
            }

            Log.d("PasswordCheck", "Password: $password, ConfirmPassword: $confirmPassword")

            if (password != confirmPassword) {
                Toast.makeText(this, "Kata Sandi dan Konfirmasi Kata Sandi Harus Sesuai", Toast.LENGTH_SHORT).show()
                return@setOnClickListener // Stop if password and confirm password are not the same
            }

            val editor = sharedPreferences.edit()
            editor.putString("REGISTERED_EMAIL", email)
            editor.putString("REGISTERED_PASSWORD", password)
            editor.apply()

            Toast.makeText(this, "Registrasi Berhasil", Toast.LENGTH_SHORT).show()

            autoLogin(email, password)
        }

        binding.tvLogin.setOnClickListener {
            // Navigate to LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun autoLogin(email: String, password: String) {
        val sharedPreferences = getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)

        val savedEmail = sharedPreferences.getString("REGISTERED_EMAIL", "")
        val savedPassword = sharedPreferences.getString("REGISTERED_PASSWORD", "")

        // Check if the saved email and password match the newly registered ones
        if (email == savedEmail && password == savedPassword) {
            Toast.makeText(this, "Login Berhasil", Toast.LENGTH_SHORT).show()

            // Navigate to HomeActivity after successful login
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        } else {
            Toast.makeText(this, "Login Failed: Invalid credentials", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }
}
