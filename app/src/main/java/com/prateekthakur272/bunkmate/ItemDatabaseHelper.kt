package com.prateekthakur272.bunkmate

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class ItemDatabaseHelper(context: Context):SQLiteOpenHelper(context, DB_NAME,null,1) {

    companion object{
        const val DB_NAME = "item_database.db"
        const val TABLE_NAME = "subjects"
        const val COLUMN_ID = "id"
        const val COLUMN_SUBJECT_NAME = "subject_name"
        const val COLUMN_ATTENDED = "lectures_attended"
        const val COLUMN_TOTAL = "total_lectures"
        const val TAG = "Database"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val query = "create table $TABLE_NAME($COLUMN_ID integer primary key autoincrement ,$COLUMN_SUBJECT_NAME varchar(30) not null,$COLUMN_ATTENDED integer default 0,$COLUMN_TOTAL integer default 0)"
        db?.execSQL(query)
        if (db!!.isOpen)
            Log.d(TAG,"Database is open")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }

    fun addItem(item:Item) {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(COLUMN_SUBJECT_NAME,item.title)
        db.insert(TABLE_NAME,null,cv)
    }


    fun getArrayList():ArrayList<Item>{
        val itemList = mutableListOf<Item>()
        val query = "select * from $TABLE_NAME"
        val db = this.readableDatabase
        try {
            val cursor = db.rawQuery(query,null)
            if (cursor.moveToFirst()){
                do {
                    itemList.add(Item(cursor.getInt(0),cursor.getString(1),cursor.getInt(2),cursor.getInt(3)))
                }while (cursor.moveToNext())
            }
        }catch (e:java.lang.RuntimeException){
            Log.d(TAG,"table not found")
        }
        db.close()
        return itemList as ArrayList<Item>
    }
}