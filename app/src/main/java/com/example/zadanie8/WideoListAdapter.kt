package com.example.zadanie8

import android.app.LauncherActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WideoListAdapter(private val mList: ArrayList<Pair<Long, String>>, private val onItemClicked: (position: Int) -> Unit) : RecyclerView.Adapter<WideoListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_row, parent, false)

        return ViewHolder(view, onItemClicked)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = mList[position]

        holder.idTV.text = ItemsViewModel.first.toString()
        holder.titleTV.text = ItemsViewModel.second
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View, private val onItemClicked: (position: Int) -> Unit) : RecyclerView.ViewHolder(ItemView), View.OnClickListener {
        val idTV: TextView = itemView.findViewById(R.id.idTextView)
        val titleTV: TextView = itemView.findViewById(R.id.titleTextView)
        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            onItemClicked(adapterPosition)
        }
    }
}