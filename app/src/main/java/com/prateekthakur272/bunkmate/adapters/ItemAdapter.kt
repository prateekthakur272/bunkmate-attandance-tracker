package com.prateekthakur272.bunkmate.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.prateekthakur272.bunkmate.database.Item
import com.prateekthakur272.bunkmate.ItemActivity
import com.prateekthakur272.bunkmate.database.ItemDatabaseHelper
import com.prateekthakur272.bunkmate.R
import com.prateekthakur272.bunkmate.databinding.ItemLayoutBinding
import kotlin.math.roundToInt

class ItemAdapter(private val context:Context):RecyclerView.Adapter<ItemAdapter.ViewHolder>(){
    private var itemDatabaseHelper = ItemDatabaseHelper(context)
    class ViewHolder(itemView: ItemLayoutBinding) :RecyclerView.ViewHolder(itemView.root){
        val item:MaterialCardView = itemView.itemView
        val title:TextView = itemView.title
        val percentage:TextView =itemView.percentage
        val cancelButton:FloatingActionButton = itemView.buttonCancel
        val markButton:FloatingActionButton = itemView.buttonMark
    }
    lateinit var items:ArrayList<Item>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.title.text = items[position].title
        holder.percentage.text = context.getString(R.string.percent_attendance,items[position].attendance.roundToInt())
        holder.markButton.setOnClickListener {
            itemDatabaseHelper.markAttendance(items[position].id,true)
            items = itemDatabaseHelper.getArrayList()
            notifyItemChanged(position)
        }
        holder.cancelButton.setOnClickListener {
            itemDatabaseHelper.markAttendance(items[position].id,false)
            items = itemDatabaseHelper.getArrayList()
            notifyItemChanged(position)
        }
        holder.item.setOnClickListener {
            val itemIntent = Intent(context, ItemActivity::class.java)
            itemIntent.putExtra("id",items[position].id)
            context.startActivity(itemIntent)
        }
    }
}