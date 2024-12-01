package com.example.cakemate

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class AdapterSimpanResep(
    // Daftar resep asli
    private val recipes: MutableList<ListResep>,
    private val context: Context
) : RecyclerView.Adapter<AdapterSimpanResep.RecipeViewHolder>(), Filterable {

    // Daftar resep yang difilter untuk recycler view saat dicari resepnya
    private var filteredRecipeList = recipes.toMutableList()
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    // ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.itemsimpanresep, parent, false)
        return RecipeViewHolder(itemView)
    }

    // Fungsi untuk mengikat data ke ViewHolder
    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        // Resep berdasarkan posisi
        val recipe = filteredRecipeList[position]
        // Menampilkan judul resep
        holder.recipeTitle.text = recipe.title
        // Menampilkan gambar resep
        holder.recipeImage.setImageResource(recipe.imageResId)
        // Menampilkan deskripsi resep
        holder.recipeDescription.text = recipe.description
        // Event listener untuk item klik
        holder.itemView.setOnClickListener {
            val intent = Intent(context, getRecipeDetailActivity(recipe.title))
            intent.putExtra("RECIPE_TITLE", recipe.title)
            intent.putExtra("RECIPE_IMAGE", recipe.imageResId)
            intent.putExtra("RECIPE_DESCRIPTION", recipe.description)
            context.startActivity(intent)
        }
    }

    // Fungsi untuk mendapatkan deatil resep berdasarkan title resep
    private fun getRecipeDetailActivity(title: String): Class<out AppCompatActivity> {
        return when (title) {
            "Almond Crispy" -> ResepAlmond::class.java
            "Jahe" -> ResepJahe::class.java
            "Cokelat Mete" -> ResepMete::class.java
            "Onde-Onde Ketawa" -> ResepOnde::class.java
            "Sagu Pandan Keju" -> ResepSagu::class.java
            else -> SimpanResep::class.java
        }
    }

    //  Memberitahu RecyclerView berapa banyak item yang harus ditampilkan di layar sesuai resep yang dicari
    override fun getItemCount(): Int = filteredRecipeList.size

    // Menambahkan resep baru ke daftar
    fun addRecipe(recipe: ListResep) {
        recipes.add(recipe)
        filteredRecipeList.add(recipe)
        notifyItemInserted(filteredRecipeList.size - 1)
    }

    // Menghapus item dari Firestore dan daftar lokal
    fun removeItemFromFirestore(position: Int, recipe: ListResep) {
        // Ngedetect pengguna saat ini
        val currentUser = auth.currentUser ?: return
        val uid = currentUser.uid
        Log.d("AdapterSimpanResep", "Menghapus resep: ${recipe.title}")

        // Query untuk mencari dokumen berdasarkan pengguna dan judul resep di firestore
        firestore.collection("Recipes")
            .whereEqualTo("userId", uid)
            .whereEqualTo("title", recipe.title)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.documents.isNotEmpty()) {
                    for (document in querySnapshot.documents) {
                        firestore.collection("Recipes").document(document.id)
                            .delete() // Menghapus dokumen dari Firestore
                            .addOnSuccessListener {
                                Log.d("AdapterSimpanResep", "Resep ${recipe.title} berhasil dihapus.")
                                // Menghapus dari daftar lokal
                                removeItem(position)
                            }
                            .addOnFailureListener { e ->
                                Log.e("AdapterSimpanResep", "Gagal menghapus resep: ${e.message}", e)
                            }
                    }
                } else {
                    Log.d("AdapterSimpanResep", "Tidak ada resep yang cocok ditemukan untuk dihapus.")
                }
            }
            .addOnFailureListener { e ->
                Log.e("AdapterSimpanResep", "Gagal menemukan resep untuk dihapus: ${e.message}", e)
            }
    }

    // Menghapus item dari daftar lokal
    fun removeItem(position: Int) {
        // Hapus data dari daftar asli
        recipes.removeAt(position)
        // Hapus data dari daftar yang difilter
        filteredRecipeList.removeAt(position)
        // Memberitahu RecyclerView bahwa item ini telah dihapus
        notifyItemRemoved(position)
    }

    // Fungsi untuk filter pencarian
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                // Query untuk pencarian
                val query = constraint?.toString()?.lowercase(Locale.getDefault()) ?: ""

                filteredRecipeList = if (query.isEmpty()) {
                    // Tidak ada filter
                    recipes.toMutableList()
                } else {
                    recipes.filter {
                        // Filter berdasarkan judul
                        it.title.lowercase(Locale.getDefault()).contains(query)
                    }.toMutableList()
                }

                return FilterResults().apply { values = filteredRecipeList }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredRecipeList = results?.values as MutableList<ListResep>
                // Memberitahu perubahan setelah filtering
                notifyDataSetChanged()
            }
        }
    }

    // Mengupdate daftar resep
    fun setRecipes(newRecipes: List<ListResep>) {
        recipes.clear()
        filteredRecipeList.clear()
        recipes.addAll(newRecipes)
        filteredRecipeList.addAll(newRecipes)
        Log.d("AdapterSimpanResep", "Resep diperbarui: ${filteredRecipeList.size}")
        notifyDataSetChanged()
    }

    // ViewHolder untuk menampilkan item
    class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recipeTitle: TextView = itemView.findViewById(R.id.recipeTitle)
        val recipeImage: ImageView = itemView.findViewById(R.id.recipeImage)
        val recipeDescription: TextView = itemView.findViewById(R.id.recipeDescription)
    }

    // Mendapatkan item berdasarkan posisi
    fun getItemAtPosition(position: Int): ListResep {
        return filteredRecipeList[position]
    }

    // Menghapus semua resep dari daftar
    fun clearRecipes() {
        recipes.clear()
        filteredRecipeList.clear()
        notifyDataSetChanged()
    }
}
