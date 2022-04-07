package com.binar.challenge4.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Schedule(
    @PrimaryKey(autoGenerate = true) var id: Int?,
    var eventType: String,
    var eventDate: String,
    var eventDateMillis:Long,
    var choosenTime:String,
    var description: String,

): Parcelable
