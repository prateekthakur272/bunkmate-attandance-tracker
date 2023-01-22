package com.prateekthakur272.bunkmate

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var itemView:RecyclerView
    private lateinit var itemAdapter:ItemAdapter
    private lateinit var addItemDialog:Dialog
    private lateinit var itemDatabaseHelper: ItemDatabaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        itemDatabaseHelper = ItemDatabaseHelper(this)
        itemView = findViewById(R.id.item_view)

        itemAdapter = ItemAdapter(this)
        itemAdapter.items = itemDatabaseHelper.getArrayList()
        itemView.adapter = itemAdapter
        itemView.layoutManager = LinearLayoutManager(this)

        addItemDialog = Dialog(this)
        addItemDialog.setCancelable(false)
        addItemDialog.setContentView(R.layout.add_item_dialog_layout)
        val addButton:MaterialButton = addItemDialog.findViewById(R.id.button_add)
        val cancelButton:MaterialButton = addItemDialog.findViewById(R.id.button_cancel)
        val subjectName:EditText = addItemDialog.findViewById(R.id.subject_name)
        addButton.setOnClickListener {
            if (subjectName.text.isNotBlank()) {
                itemDatabaseHelper.addItem(Item(subjectName.text.toString().trim()))
                addItemDialog.dismiss()
                onRestart()
                Snackbar.make(itemView, "Added", Snackbar.LENGTH_SHORT).show()
            }
            else {
                Snackbar.make(itemView, "Enter a valid subject name", Snackbar.LENGTH_SHORT).show()
            }
            subjectName.text.clear()
        }
        cancelButton.setOnClickListener {
            subjectName.text.clear()
            addItemDialog.cancel()
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

            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onRestart() {
        super.onRestart()
        itemAdapter.items = itemDatabaseHelper.getArrayList()
        itemView.adapter = itemAdapter
    }
}