package com.prateekthakur272.bunkmate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.prateekthakur272.bunkmate.adapters.HistoryItemAdapter
import com.prateekthakur272.bunkmate.database.Item
import com.prateekthakur272.bunkmate.database.ItemDatabaseHelper
import com.prateekthakur272.bunkmate.databinding.ActivityItemBinding
import org.eazegraph.lib.models.PieModel
import kotlin.math.roundToInt

class ItemActivity : AppCompatActivity() {
    private lateinit var binding: ActivityItemBinding
    private lateinit var itemDatabaseHelper: ItemDatabaseHelper
    private lateinit var subject: Item
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        itemDatabaseHelper = ItemDatabaseHelper(this)
        subject = itemDatabaseHelper.getItem(intent.getIntExtra("id",-1))
        supportActionBar?.title = subject.title
        if (subject.totalLectures!=0) {
            binding.pieChart.addPieSlice(
                PieModel(
                    (100 - subject.attendance),
                    this.getColor(R.color.red)
                )
            )
            binding.pieChart.addPieSlice(
                PieModel(
                    subject.attendance,
                    this.getColor(R.color.green)
                )
            )
        }else{
            binding.pieChart.addPieSlice(
                PieModel(
                    100f,
                    this.getColor(R.color.black)
                )
            )
            binding.percentageAttendance.visibility = View.GONE
        }

        binding.lecturesAttended.text = getString(R.string.total_lectures_attended,subject.lectureAttended)
        binding.lecturesConducted.text = getString(R.string.total_lectures_conducted,subject.totalLectures)
        binding.percentageAttendance.text = subject.attendance.roundToInt().toString()
        if (subject.attendance<75)
            binding.percentageAttendance.setTextColor(getColor(R.color.red))
        val adapter = HistoryItemAdapter(this,itemDatabaseHelper.getHistory(subject.title))
        binding.historyView.adapter = adapter
        binding.historyView.layoutManager = LinearLayoutManager(this)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.item_menu,menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.delete ->{
                val dialog = AlertDialog.Builder(this)
                dialog.setTitle("Do you want to delete ${subject.title}?")
                dialog.setCancelable(false)
                dialog.setPositiveButton("Delete") { _, i ->
                    itemDatabaseHelper.deleteItem(subject.id)
                    finish()
                }
                dialog.setNegativeButton("No") { _, _ -> }
                dialog.create().show()
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