package com.prateekthakur272.bunkmate

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class ItemAdapter(context:Context):RecyclerView.Adapter<ItemAdapter.ViewHolder>(){

    class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        val item:MaterialCardView = itemView.findViewById(R.id.item_view)
        val title:TextView = itemView.findViewById(R.id.title)
        val percentage:TextView =itemView.findViewById(R.id.percentage)
        val cancelButton:FloatingActionButton = itemView.findViewById(R.id.button_cancel)
        val markButton:FloatingActionButton = itemView.findViewById(R.id.button_mark)
    }

    lateinit var items:ArrayList<Item>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_layout,parent,false))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = items[position].title
        holder.percentage.text = items[position].percentage.toString()

        holder.item.setOnClickListener {
            Snackbar.make(holder.item,items[position].title,Snackbar.LENGTH_SHORT).show()
        }
    }
}