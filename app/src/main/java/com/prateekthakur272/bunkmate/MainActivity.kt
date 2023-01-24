package com.prateekthakur272.bunkmate

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.prateekthakur272.bunkmate.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding
    private lateinit var itemAdapter:ItemAdapter
    private lateinit var addItemDialog:Dialog
    private lateinit var aboutDialog: Dialog
    private lateinit var itemDatabaseHelper: ItemDatabaseHelper
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        itemDatabaseHelper = ItemDatabaseHelper(this)

        itemAdapter = ItemAdapter(this)
        itemAdapter.items = itemDatabaseHelper.getArrayList()
        binding.itemView.adapter = itemAdapter
        binding.itemView.layoutManager = LinearLayoutManager(this)

        addItemDialog = Dialog(this)
        addItemDialog.setCancelable(false)
        addItemDialog.setContentView(R.layout.add_item_dialog_layout)
        val addButton:MaterialButton = addItemDialog.findViewById(R.id.button_add)
        val cancelButton:MaterialButton = addItemDialog.findViewById(R.id.button_cancel)
        val subjectName:EditText = addItemDialog.findViewById(R.id.subject_name)
        val classesAttended:EditText = addItemDialog.findViewById(R.id.lectures_attended)
        val classesConducted:EditText = addItemDialog.findViewById(R.id.lectures_conducted)
        addButton.setOnClickListener {

            val classesConductedInput = if (classesConducted.text.isEmpty()) 0 else classesConducted.text.toString().toInt()
            val classesAttendedInput = if (classesAttended.text.isEmpty()) 0 else classesAttended.text.toString().toInt()

            if (subjectName.text.isBlank()) {
                Toast.makeText(this,"Subject name cannot be empty",Toast.LENGTH_SHORT).show()
            }
            else if (classesConductedInput < classesAttendedInput){
                Toast.makeText(this,"Classes conducted should be greater than classes attended",Toast.LENGTH_LONG).show()
                classesAttended.text.clear()
                classesConducted.text.clear()
            }
            else {
                itemDatabaseHelper.addItem(Item(subjectName.text.toString().trim(),classesAttendedInput,classesConductedInput))
                addItemDialog.dismiss()
                onRestart()
                Snackbar.make(binding.itemView, "Added", Snackbar.LENGTH_SHORT).show()
                subjectName.text.clear()
                classesAttended.text.clear()
                classesConducted.text.clear()
            }
        }
        cancelButton.setOnClickListener {
            subjectName.text.clear()
            classesAttended.text.clear()
            classesConducted.text.clear()
            addItemDialog.cancel()
        }
        aboutDialog = Dialog(this)
        aboutDialog.setContentView(R.layout.about_dialog_layout)
        aboutDialog.setCancelable(false)
        aboutDialog.findViewById<MaterialButton>(R.id.contact_me).setOnClickListener {
            startActivity(Intent(this,ContactMeActivity::class.java))
        }
        aboutDialog.findViewById<MaterialButton>(R.id.cancel).setOnClickListener {
            aboutDialog.dismiss()
        }
        with(binding.noItemMessage){
            if (itemAdapter.itemCount==0)
                this.visibility = View.VISIBLE
            else
                this.visibility = View.GONE
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.add_item -> {
                addItemDialog.show()
                return true
            }
            R.id.about -> {
                aboutDialog.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onRestart() {
        super.onRestart()
        itemAdapter.items = itemDatabaseHelper.getArrayList()
        binding.itemView.adapter = itemAdapter

        with(binding.noItemMessage){
            if (itemAdapter.itemCount==0)
                this.visibility = View.VISIBLE
            else
                this.visibility = View.GONE
        }
    }
}