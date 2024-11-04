package com.example.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CookieAdapter(private val cookies: List<Cookie>) :
    RecyclerView.Adapter<CookieAdapter.CookieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CookieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cookie, parent, false)
        return CookieViewHolder(view)
    }

    override fun onBindViewHolder(holder: CookieViewHolder, position: Int) {
        val cookie = cookies[position]
        holder.cookieName.text = cookie.name
        holder.cookieImage.setImageResource(cookie.imageResId)
    }

    override fun getItemCount(): Int = cookies.size

    inner class CookieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cookieImage: ImageView = itemView.findViewById(R.id.cookieImage)
        val cookieName: TextView = itemView.findViewById(R.id.cookieName)
    }
}
