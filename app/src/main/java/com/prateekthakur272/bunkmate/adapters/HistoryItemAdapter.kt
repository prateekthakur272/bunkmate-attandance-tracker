package com.prateekthakur272.bunkmate.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.prateekthakur272.bunkmate.R
import com.prateekthakur272.bunkmate.database.HistoryItem
import com.prateekthakur272.bunkmate.databinding.HistoryItemLayoutBinding

class HistoryItemAdapter(val context: Context, var historyItemList: ArrayList<HistoryItem>):RecyclerView.Adapter<HistoryItemAdapter.ViewHolder>(){
    class ViewHolder(itemView: HistoryItemLayoutBinding) :RecyclerView.ViewHolder(itemView.root){
        val binder = itemView
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(HistoryItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }
    override fun getItemCount(): Int = historyItemList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binder){
            title.text = historyItemList[position].title
            dateTime.text = historyItemList[position].dateTime
            status.text = historyItemList[position].status
            if (historyItemList[position].status == "Attended")
                status.setTextColor(context.getColor(R.color.green))
            else if(historyItemList[position].status == "Missed")
                status.setTextColor(context.getColor(R.color.red))
        }
        if (historyItemList.all { it.title == historyItemList[position].title })
            holder.binder.title.visibility = View.GONE
    }
}