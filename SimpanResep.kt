package com.example.cakemate

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cakemate.databinding.SimpanResepBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SimpanResep : AppCompatActivity() {

    private lateinit var adapterSimpanResep: AdapterSimpanResep
    private lateinit var binding: SimpanResepBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Menggunakan View Binding untuk menghubungkan layout activity
        binding = SimpanResepBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi Firebase Authentication dan Firestorenya
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Inisialisasi RecyclerView dengan adapter kosong
        val recipes = mutableListOf<ListResep>()
        adapterSimpanResep = AdapterSimpanResep(recipes, this)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapterSimpanResep

        // Mengaktifkan geser untuk menghapus resep
        val itemTouchHelper = ItemTouchHelper(HapusSimpanResep(adapterSimpanResep))
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)

        // Memeriksa apakah pengguna sudah login
        val currentUser = auth.currentUser
        if (currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        // Mendapatkan UID pengguna yang sedang login
        val uid = currentUser.uid

        // Mengambil data resep yang disimpan dari Firestore
        fetchSavedRecipes(uid)

        // Memproses data resep baru yang dikirim melalui Intent
        val newRecipeTitle = intent.getStringExtra("RESEP_TITLE")
        val newRecipeImage = intent.getIntExtra("RESEP_IMAGE", -1)
        val newRecipeDescription = intent.getStringExtra("RESEP_DESCRIPTION")

        // Menambahkan resep baru jika informasi tersedia
        if (newRecipeTitle != null && newRecipeImage != -1 && newRecipeDescription != null) {
            val newRecipe = ListResep(newRecipeTitle, newRecipeImage, newRecipeDescription)
            saveRecipeToFirestore(uid, newRecipe)
        }

        // Mengatur fitur pencarian untuk ngefilter daftar resep
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                // Filter data dalam adapter berdasarkan teks pencarian
                adapterSimpanResep.filter.filter(newText)
                return false
            }
        })

        // Mengatur navigasi di BottomNavigationView
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            if (bottomNavigation.selectedItemId != item.itemId) {
                when (item.itemId) {
                    R.id.nav_dashboard -> {
                        startActivity(Intent(this, Dashboard::class.java))
                        finish()
                    }
                    R.id.nav_menu -> {
                        startActivity(Intent(this, MainActivityDashboard::class.java))
                        finish()
                    }
                    R.id.nav_upload -> {
                        startActivity(Intent(this, Upload1::class.java))
                        finish()
                    }
                }
            }
            true
        }
        // Menandai tab saat ini di navigasi bawah
        bottomNavigation.selectedItemId = R.id.nav_save_recipe
    }

    // Dipanggil setiap kali activity dimulai
    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // Mengambil resep terbaru untuk pengguna yang sedang login
            fetchSavedRecipes(currentUser.uid)
        }
    }

    // Mengambil resep yang disimpan dari Firestore untuk pengguna tertentu
    private fun fetchSavedRecipes(uid: String) {
        Log.d("SimpanResep", "Mengambil resep untuk pengguna $uid")
        firestore.collection("Recipes")
            .whereEqualTo("userId", uid)
            .get()
            .addOnSuccessListener { querySnapshot ->
                // Menghapus daftar resep saat ini untuk menghindari duplikasi
                adapterSimpanResep.clearRecipes()

                val recipes = mutableListOf<ListResep>()
                for (document in querySnapshot.documents) {
                    // Mengambil data resep dari setiap dokumen
                    val title = document.getString("title") ?: continue
                    val description = document.getString("description") ?: continue
                    val imageResId = document.getLong("imageResId")?.toInt() ?: -1
                    recipes.add(ListResep(title, imageResId, description))
                }
                Log.d("SimpanResep", "Mengambil ${recipes.size} resep untuk pengguna $uid")
                // Memperbarui adapter dengan daftar resep yang diambil
                adapterSimpanResep.setRecipes(recipes)
            }
            .addOnFailureListener { e ->
                Log.e("SimpanResep", "Gagal mengambil resep: ${e.message}", e)
            }
    }

    // Menyimpan resep baru ke Firestore
    private fun saveRecipeToFirestore(uid: String, newRecipe: ListResep) {
        firestore.collection("Recipes")
            .whereEqualTo("userId", uid)
            .whereEqualTo("title", newRecipe.title)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) {
                    // Menambahkan resep jika belum ada
                    val recipeData = mapOf(
                        "userId" to uid,
                        "title" to newRecipe.title,
                        "description" to newRecipe.description,
                        "imageResId" to newRecipe.imageResId
                    )
                    firestore.collection("Recipes")
                        .add(recipeData)
                        .addOnSuccessListener {
                            // Mengambil ulang daftar resep setelah berhasil disimpan
                            fetchSavedRecipes(uid)
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(
                                this,
                                "Gagal menyimpan resep: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                } else {
                    // Jika resep sudah ada, tampilkan pesan ke pengguna.
                    Toast.makeText(this, "Resep sudah ada di simpan.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    this,
                    "Gagal memeriksa resep yang ada: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}

