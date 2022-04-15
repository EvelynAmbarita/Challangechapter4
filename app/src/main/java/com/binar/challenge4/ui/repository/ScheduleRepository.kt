package com.binar.challenge4.ui.repository

import android.content.Context
import com.binar.challenge4.data.Schedule
import com.binar.challenge4.database.MyDatabase
import kotlinx.coroutines.coroutineScope

class ScheduleRepository(private val context: Context) {

    private var myDatabase = MyDatabase.getInstance(context)

    suspend fun getAllSchedule(): List<Schedule>? = coroutineScope {
        return@coroutineScope myDatabase?.scheduleDao()?.getAllSchedule()
    }

    suspend fun getSelectedDaySchedule(eventDate: String): List<Schedule>? = coroutineScope {
        return@coroutineScope myDatabase?.scheduleDao()?.getSelectedDaySchedule(eventDate)
    }

    suspend fun addSchedule(schedule: Schedule):Long? = coroutineScope {
        return@coroutineScope myDatabase?.scheduleDao()?.insertSchedule(schedule)
    }

    suspend fun updateSchedule(schedule: Schedule):Int? = coroutineScope {
        return@coroutineScope myDatabase?.scheduleDao()?.updateSchedule(schedule)
    }

    suspend fun deleteSchedule(schedule: Schedule):Int? = coroutineScope {
        return@coroutineScope myDatabase?.scheduleDao()?.deleteSchedule(schedule)
    }

}