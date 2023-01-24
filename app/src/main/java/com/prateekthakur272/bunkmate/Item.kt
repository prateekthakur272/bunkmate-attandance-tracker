package com.prateekthakur272.bunkmate

data class Item(val title:String,var lectureAttended:Int = 0,var totalLectures:Int = 0){
    var id: Int = -1
    var attendance:Float = 0.0f
    constructor(id:Int,title: String,lectureAttended:Int,totalLectures:Int) : this(title){
        this.id = id
        this.lectureAttended = lectureAttended
        this.totalLectures = totalLectures
        this.attendance = if (totalLectures!=0) (lectureAttended.toFloat()/totalLectures)*100 else 100f
    }
}