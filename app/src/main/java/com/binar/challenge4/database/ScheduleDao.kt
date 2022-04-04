package com.binar.challenge4.database

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.binar.challenge4.data.Schedule

@Dao
interface ScheduleDao {
    @Query("SELECT * FROM Schedule")
    fun getAllSchedule():List<Schedule>

    @Insert(onConflict = REPLACE)
    fun insertSchedule(schedule: Schedule):Long

    @Update
    fun updateSchedule(schedule: Schedule):Int

    @Delete
    fun deleteSchedule(schedule: Schedule):Int
}