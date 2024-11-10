package com.example.catatan

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.appcompat.widget.SearchView
import com.example.catatan.databinding.ActivityMainNotesBinding

class MainActivityNotes : AppCompatActivity() {

    private lateinit var notesAdapter: NotesAdapter
    private lateinit var binding: ActivityMainNotesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // List catatan
        val notes = listOf(
            Notes("Notes 1", "Resep-resep rahasia makanan dari pulau Sumatera, bahan-bahan berikut harus saya beli", R.drawable.pic1),
            Notes("Catatan Belanja", "Saya harus belanja pada hari ini, bahan yang saya perlukan untuk membuat kue tradisional ini adalah\n" +
                    "Tepung (250g)", R.drawable.pic2),
            Notes("Saran Kawan", "Kue tradisional yang terbuat dari tepung beras dan gula merah.", R.drawable.pic3),
            Notes("Resep Temen", "Kue manis berwarna coklat ini dibuat dari tepung beras dan gula merah, memiliki tekstur yang tebal.", R.drawable.pic4),
            Notes("Bahan Pengawet", "Terbuat dari tepung beras yang dibentuk menyerupai mie.", R.drawable.pic5)
        )

        // Inisialisasi adapter dan RecyclerView
        notesAdapter = NotesAdapter(notes)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = notesAdapter

        // Setup SearchView
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                notesAdapter.filter.filter(newText)
                return false
            }
        })
    }
}
