package com.lab5.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lab5.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val sharedPrefFile = "com.lab5.myapplication.PREFERENCE_FILE_KEY"

    private var isPasswordVisible = false
    private var isConfirmPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.etUsername.requestFocus()
        showKeyboard(binding.etUsername)

        val sharedPreferences = getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)

        binding.ivToggleEtPassword.setOnClickListener {
            togglePasswordVisibility(
                binding.etPassword,
                binding.ivToggleEtPassword,
                isPasswordVisible
            ) { isPasswordVisible = it }
        }

        binding.ivToggleConfirmPassword.setOnClickListener {
            togglePasswordVisibility(
                binding.etConfirmPassword,
                binding.ivToggleConfirmPassword,
                isConfirmPasswordVisible
            ) { isConfirmPasswordVisible = it }
        }

        binding.btnRegister.setOnClickListener {
            val email = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()

            val alphanumericWithSpacesRegex = "^[a-zA-Z0-9 ]{5,35}$".toRegex()

            when {
                email.isEmpty() -> {
                    Toast.makeText(this, "Masukkan Email Terlebih Dahulu", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                !alphanumericWithSpacesRegex.matches(email) -> {
                    Toast.makeText(
                        this,
                        "Nama Pengguna Harus Terdiri Dari 5-35 Huruf, Angka, atau Spasi",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
                password.isEmpty() -> {
                    Toast.makeText(this, "Masukkan Kata Sandi Terlebih Dahulu", Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }
                password != confirmPassword -> {
                    Toast.makeText(
                        this,
                        "Kata Sandi dan Konfirmasi Kata Sandi Harus Sesuai",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
            }

            val editor = sharedPreferences.edit()
            editor.putString("REGISTERED_USERNAME", email)
            editor.putString("REGISTERED_PASSWORD", password)
            editor.apply()

            Toast.makeText(this, "Registrasi Berhasil", Toast.LENGTH_SHORT).show()

            autoLogin(email, password)
        }

        binding.tvLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun autoLogin(email: String, password: String) {
        val sharedPreferences = getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)

        val savedUsername = sharedPreferences.getString("REGISTERED_USERNAME", "")
        val savedPassword = sharedPreferences.getString("REGISTERED_PASSWORD", "")

        if (email == savedUsername && password == savedPassword) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        } else {
            Toast.makeText(this, "Login Failed: Invalid credentials", Toast.LENGTH_SHORT).show()
        }
    }

    private fun togglePasswordVisibility(
        editText: EditText,
        toggleImageView: ImageView,
        isCurrentlyVisible: Boolean,
        updateVisibilityState: (Boolean) -> Unit
    ) {
        if (isCurrentlyVisible) {
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            toggleImageView.setImageResource(R.drawable.eyepw)
            updateVisibilityState(false)
        } else {
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            toggleImageView.setImageResource(R.drawable.eyehidden)
            updateVisibilityState(true)
        }
        editText.setSelection(editText.text.length)
    }

    private fun showKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }
}
