package com.prateekthakur272.bunkmate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var itemView:RecyclerView
    private lateinit var itemAdapter:ItemAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        itemView = findViewById(R.id.item_view)

        itemAdapter = ItemAdapter(this)
        itemAdapter.items = arrayListOf(
            Item("Java"),
            Item("Python"),
            Item("Universal Human Values and Education"),
            Item("Software Engineering")
        )
        itemView.adapter = itemAdapter
        itemView.layoutManager = LinearLayoutManager(this)
    }
}