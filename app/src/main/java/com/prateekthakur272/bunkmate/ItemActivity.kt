package com.prateekthakur272.bunkmate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.prateekthakur272.bunkmate.databinding.ActivityItemBinding
import org.eazegraph.lib.models.PieModel
import kotlin.math.roundToInt

class ItemActivity : AppCompatActivity() {
    private lateinit var binding: ActivityItemBinding
    private lateinit var itemDatabaseHelper: ItemDatabaseHelper
    private lateinit var subject:Item
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        itemDatabaseHelper = ItemDatabaseHelper(this)
        subject = itemDatabaseHelper.getItem(intent.getIntExtra("id",-1))
        supportActionBar?.title = subject.title

        binding.pieChart.addPieSlice(
            PieModel(
                subject.lectureAttended.toFloat(),
                this.getColor(R.color.green)
            )
        )
        binding.pieChart.addPieSlice(
            PieModel(
                (subject.totalLectures-subject.lectureAttended).toFloat(),
                this.getColor(R.color.red)
            )
        )

        binding.lecturesAttended.text = getString(R.string.total_lectures_attended,subject.lectureAttended)
        binding.lecturesConducted.text = getString(R.string.total_lectures_conducted,subject.totalLectures)
        binding.percentageAttendance.text = subject.attendance.roundToInt().toString()
        if (subject.attendance<75)
            binding.percentageAttendance.setTextColor(getColor(R.color.red))
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.item_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.delete ->{
                itemDatabaseHelper.deleteItem(subject.id)
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}