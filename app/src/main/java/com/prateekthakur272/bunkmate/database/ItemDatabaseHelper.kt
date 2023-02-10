package com.prateekthakur272.bunkmate.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class ItemDatabaseHelper(context: Context):SQLiteOpenHelper(context, DB_NAME,null,1) {

    companion object{
        const val DB_NAME = "attendance_database.db"
        const val TABLE_ATTENDANCE = "attendance"
        const val COLUMN_SUBJECT_ID = "subject_id"
        const val COLUMN_SUBJECT_NAME = "subject_name"
        const val COLUMN_ATTENDED = "attended_lectures"
        const val COLUMN_TOTAL = "total_lectures"

        const val TABLE_HISTORY = "attendance_history"
        const val COLUMN_DATE_TIME = "date_time"
        const val COLUMN_STATUS = "status"

        const val TRIGGER_ADD_HISTORY_ATTENDED = "add_to_history_attended"
        const val TRIGGER_ADD_HISTORY_MISSED = "add_to_history_missed"

        const val TAG = "database_log"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createAttendanceTable = "create table $TABLE_ATTENDANCE($COLUMN_SUBJECT_ID integer primary key autoincrement ,$COLUMN_SUBJECT_NAME varchar(30) not null,$COLUMN_ATTENDED integer default 0,$COLUMN_TOTAL integer default 0);"
        val createHistoryTable = "create table $TABLE_HISTORY($COLUMN_DATE_TIME text primary key,$COLUMN_SUBJECT_ID integer references $TABLE_ATTENDANCE($COLUMN_SUBJECT_ID) on delete cascade,$COLUMN_STATUS text);"
        val createAddHistoryAttendedTrigger = "create trigger $TRIGGER_ADD_HISTORY_ATTENDED after update on $TABLE_ATTENDANCE when new.$COLUMN_ATTENDED > old.$COLUMN_ATTENDED and new.$COLUMN_TOTAL > old.$COLUMN_TOTAL begin insert into $TABLE_HISTORY values(datetime('now','localtime'),old.subject_id,'Attended');end;"
        val createAddHistoryMissedTrigger = "create trigger $TRIGGER_ADD_HISTORY_MISSED after update on $TABLE_ATTENDANCE when new.$COLUMN_ATTENDED = old.$COLUMN_ATTENDED and new.$COLUMN_TOTAL > old.$COLUMN_TOTAL begin insert into $TABLE_HISTORY values(datetime('now','localtime'),old.subject_id,'Missed');end;"
        try {
            with(db!!){
                execSQL(createAttendanceTable)
                execSQL(createHistoryTable)
                execSQL(createAddHistoryAttendedTrigger)
                execSQL(createAddHistoryMissedTrigger)
            }
        }catch (e:java.lang.RuntimeException){
            Log.d(TAG,"Some error occurred while creating tables")
        }
        finally {
            if (db!!.isOpen)
                Log.d(TAG,"Database is open")
        }
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }

    fun addItem(item: Item) {
        val db = this.writableDatabase
        val cv = ContentValues()
        try{
            cv.put(COLUMN_SUBJECT_NAME,item.title)
            cv.put(COLUMN_ATTENDED,item.lectureAttended)
            cv.put(COLUMN_TOTAL,item.totalLectures)
            db.insert(TABLE_ATTENDANCE,null,cv)
        }catch (e:java.lang.RuntimeException){
            Log.d(TAG,"Some error occurred while inserting into tables")
        }
        finally {
            db.close()
        }
    }
    fun markAttendance(id:Int,attended:Boolean = false){
        val db = this.writableDatabase
        try {
            val query = if (attended) "update $TABLE_ATTENDANCE set $COLUMN_ATTENDED = $COLUMN_ATTENDED+1,$COLUMN_TOTAL = $COLUMN_TOTAL+1 where $COLUMN_SUBJECT_ID = $id"
            else "update $TABLE_ATTENDANCE set $COLUMN_TOTAL = $COLUMN_TOTAL+1 where $COLUMN_SUBJECT_ID = $id"
            db.execSQL(query)
        }catch (e:java.lang.RuntimeException){
            Log.d(TAG,"error while updating values")
        }finally {
            db.close()
        }
    }
    fun deleteItem(id: Int){
        val query = "delete from $TABLE_ATTENDANCE where $COLUMN_SUBJECT_ID = $id"
        try {
            this.writableDatabase.execSQL(query)
        }catch (e:java.lang.RuntimeException){
            Log.d(TAG,"error while deleting a row from table")
        }
    }
    fun getItem(id: Int): Item {
        val query = "select * from $TABLE_ATTENDANCE where $COLUMN_SUBJECT_ID = $id"
        val db = this.readableDatabase
        try {
            val cursor = db.rawQuery(query,null)
            cursor.moveToFirst()
            val item = Item(cursor.getInt(0),cursor.getString(1),cursor.getInt(2),cursor.getInt(3))
            cursor.close()
            db.close()
            return item
        }
        catch (e:java.lang.RuntimeException){
            db.close()
        }finally {
            if (db.isOpen)
                db.close()
        }
        return Item(-1,"NO SUBJECT",0,0)
    }
    fun getArrayList():ArrayList<Item>{
        val itemList = mutableListOf<Item>()
        val query = "select * from $TABLE_ATTENDANCE"
        val db = this.readableDatabase
        try {
            val cursor = db.rawQuery(query,null)
            if (cursor.moveToFirst()){
                do {
                    itemList.add(Item(cursor.getInt(0),cursor.getString(1),cursor.getInt(2),cursor.getInt(3)))
                }while (cursor.moveToNext())
            }
            cursor.close()
        }catch (e:java.lang.RuntimeException){
            Log.d(TAG,"error while fetching data from table")
        }finally {
            db.close()
        }
        return itemList as ArrayList<Item>
    }
    fun getAllHistory(): java.util.ArrayList<HistoryItem> {
        val history = mutableListOf<HistoryItem>()
        val query = "select a.$COLUMN_SUBJECT_NAME,h.$COLUMN_DATE_TIME,h.$COLUMN_STATUS from $TABLE_HISTORY h join $TABLE_ATTENDANCE a where h.$COLUMN_SUBJECT_ID = a.$COLUMN_SUBJECT_ID"
        val db = readableDatabase
        try {
            val cursor = db.rawQuery(query,null)
            if(cursor.moveToFirst())
                do {
                    with(cursor){
                        history.add(0,HistoryItem(getString(0),getString(1),getString(2)))
                    }
                }while (cursor.moveToNext())
            cursor.close()
        }catch (e:java.lang.RuntimeException){
            Log.d(TAG,"error while fetching data from table")
        }
        finally {
            db.close()
        }
        return history as ArrayList
    }
    fun getHistory(title:String): java.util.ArrayList<HistoryItem> {
        val history = mutableListOf<HistoryItem>()
        val query = "select * from (select a.$COLUMN_SUBJECT_NAME,h.$COLUMN_DATE_TIME,h.$COLUMN_STATUS from $TABLE_HISTORY h join $TABLE_ATTENDANCE a where h.$COLUMN_SUBJECT_ID = a.$COLUMN_SUBJECT_ID) where $COLUMN_SUBJECT_NAME = '$title'"
        val db = readableDatabase
        try{
            val cursor = db.rawQuery(query,null)
            if(cursor.moveToFirst())
                do {
                    with(cursor){
                        history.add(0, HistoryItem(getString(0),getString(1),getString(2)))
                    }
                }while (cursor.moveToNext())
            cursor.close()
        }catch (e:java.lang.RuntimeException){
            Log.d(TAG,"error while fetching data from table")
        }
        finally {
            db.close()
        }
        return history as ArrayList
    }
}