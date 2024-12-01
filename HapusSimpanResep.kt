package com.example.cakemate

import android.graphics.Canvas
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

// Bisa dihapus dengan cara geser ke kiri atau ke kanan
class HapusSimpanResep(private val adapter: AdapterSimpanResep) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    // Method untuk menangani perpindahan item di RecyclerView
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    // Method untuk menangani aksi saat item digeser
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        // Mendapatkan posisi item yang di-geser
        val position = viewHolder.adapterPosition
        // Mencoba mendapatkan data item resep pada posisi yang digeser
        val recipe = try {
            adapter.getItemAtPosition(position)
        } catch (e: Exception) {
            // Menampilkan notif jika terjadi error.
            Toast.makeText(viewHolder.itemView.context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            return // Menghentikan proses jika terjadi error
        }
// Memanggil fungsi untuk menghapus item dari Firestore dan RecyclerView melalui adapter
        adapter.removeItemFromFirestore(position, recipe)
    }

    // Memanggil fungsi untuk menghapus item dari Firestore dan RecyclerView melalui adapter
    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}




