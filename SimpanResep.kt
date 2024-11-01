package com.example.simpanresep

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.simpanresep.databinding.SimpanResepBinding

class SimpanResep : AppCompatActivity() {

    private lateinit var adapterSimpanResep: AdapterSimpanResep
    private lateinit var binding: SimpanResepBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = SimpanResepBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recipes = mutableListOf(
            ListResep("Kue Jahe", "Kue jahe mengacu pada kategori luas makanan yang dipanggang, biasanya dibumbui dengan jahe, cengkeh, pala, dan kayu manis, yang diberi pemanis seperti madu, gula, atau molase.", R.drawable.kuejahe),
            ListResep("Coklat Mete", "Kue kering rasa coklat diberi topping mete di atas nya.", R.drawable.coklatmete),
            ListResep("Almond Crispy", "Kue almond crispy adalah sejenis kue tipis dan renyah yang biasanya terbuat dari bahan utama seperti tepung, mentega, gula, putih telur, dan irisan almond.", R.drawable.almond),
        )

        adapterSimpanResep = AdapterSimpanResep(recipes)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapterSimpanResep

        val itemTouchHelper = ItemTouchHelper(HapusSimpanResep(adapterSimpanResep))
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapterSimpanResep.filter.filter(newText)
                return false
            }
        })
    }
}
