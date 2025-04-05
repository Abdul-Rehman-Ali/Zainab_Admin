package com.zainab_admin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SeeAllUserAdapter(
    private val context: Context,
    private val itemList: List<SeeAllUserModel>
) : RecyclerView.Adapter<SeeAllUserAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvUserName: TextView = itemView.findViewById(R.id.tvUserName)
        val tvUserEmail: TextView = itemView.findViewById(R.id.tvUserEmail)
        val tvUserPhone: TextView = itemView.findViewById(R.id.tvUserPhone)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.rv_see_all_users, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.tvUserName.text = item.name
        holder.tvUserEmail.text = item.email
        holder.tvUserPhone.text = item.phone
    }

    override fun getItemCount(): Int = itemList.size
}
