package com.prateekthakur272.bunkmate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.prateekthakur272.bunkmate.adapters.HistoryItemAdapter
import com.prateekthakur272.bunkmate.databinding.ActivityHistoryBinding

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding:ActivityHistoryBinding
    private lateinit var adapter: HistoryItemAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = HistoryItemAdapter(arrayListOf())
        binding.historyView.adapter = adapter
        binding.historyView.layoutManager = LinearLayoutManager(this)
    }
}