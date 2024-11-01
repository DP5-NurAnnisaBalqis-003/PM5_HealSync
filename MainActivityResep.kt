package com.example.resep

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivityResep : AppCompatActivity() {

    private lateinit var etUlasan: EditText
    private lateinit var ratingBar: RatingBar
    private lateinit var btnSubmit: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mainresep)

        etUlasan = findViewById(R.id.etUlasan)
        ratingBar = findViewById(R.id.ratingBar)
        btnSubmit = findViewById(R.id.btnSubmit)

        btnSubmit.setOnClickListener {
            val ulasan = etUlasan.text.toString()
            val rating = ratingBar.rating

            if (ulasan.isNotEmpty()) {
                Toast.makeText(this, "Ulasan: $ulasan\nRating: $rating", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Masukkan ulasan terlebih dahulu!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
