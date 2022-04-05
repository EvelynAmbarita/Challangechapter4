package com.binar.challenge4.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Schedule(
    @PrimaryKey(autoGenerate = true) var id: Int?,
    var event_type: String
//    var description: String,
//    var date_millis: Long,
//    var start_time: String,
//    var end_time: String
): Parcelable
