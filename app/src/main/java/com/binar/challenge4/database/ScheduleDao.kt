package com.binar.challenge4.database

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.binar.challenge4.data.Schedule

@Dao
interface ScheduleDao {
    @Query("SELECT * FROM Schedule ORDER BY eventDateMillis DESC")
    fun getAllSchedule():List<Schedule>

    @Query("SELECT * FROM Schedule WHERE eventDate = :eventDate")
    fun getSelectedDaySchedule(eventDate: String):List<Schedule>

    @Insert(onConflict = REPLACE)
    fun insertSchedule(schedule: Schedule):Long

    @Update
    fun updateSchedule(schedule: Schedule):Int

    @Delete
    fun deleteSchedule(schedule: Schedule):Int
}