package com.prateekthakur272.bunkmate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.prateekthakur272.bunkmate.databinding.ActivityTimeTableBinding

class TimeTableActivity : AppCompatActivity() {
    private lateinit var binding:ActivityTimeTableBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimeTableBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}