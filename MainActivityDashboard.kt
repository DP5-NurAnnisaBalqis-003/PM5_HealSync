package com.example.projectuas

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.appcompat.widget.SearchView
import com.example.projectuas.databinding.ActivityMainDashboardBinding

class MainActivityDashboard : AppCompatActivity() {

    private lateinit var recipeAdapter: RecipeAdapter
    private lateinit var binding: ActivityMainDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recipes = listOf(
            Recipe("Jahe", "Kue jahe mengacu pada kategori luas makanan yang dipanggang, biasanya dibumbui dengan jahe, cengkeh, pala, dan kayu manis, yang diberi pemanis seperti madu, gula, atau molase.", R.drawable.jahe_image),
            Recipe("Onde-onde Ketawa", "Kue ketawa adalah camilan khas Kota Pematangsiantar, Sumatera Utara yang berbentuk bulat dengan belahan di tengahnya sehingga terlihat seperti tersenyum atau tertawa.", R.drawable.ondeonde_image),
            Recipe("Cokelat Mete", "Kue kering rasa cokelat diberi topping mete di atas nya.", R.drawable.cokelat_image),
            Recipe("Almond Crispy", "Kue almond crispy adalah sejenis kue tipis dan renyah yang biasanya terbuat dari bahan utama seperti tepung, mentega, gula, putih telur, dan irisan almond.", R.drawable.almond_image),
            Recipe("Sagu Pandan Keju", "Sagu pandan keju adalah kue kering yang terbuat dari tepung sagu dengan rasa pandan dan taburan keju.", R.drawable.sagu_image),
            Recipe("Kacang", "Kue kacang adalah jenis kue kering yang berbahan dasar kacang tanah, tepung, gula, dan minyak atau margarin. Kue ini memiliki tekstur yang renyah dan rasa yang gurih-manis dengan aroma kacang yang khas.", R.drawable.kacang_image)
        )

        recipeAdapter = RecipeAdapter(recipes)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = recipeAdapter

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                recipeAdapter.filter.filter(newText)
                return false
            }
        })
    }
}
