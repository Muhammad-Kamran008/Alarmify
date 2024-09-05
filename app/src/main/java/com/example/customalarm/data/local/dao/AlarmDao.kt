package com.example.customalarm.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.customalarm.data.local.entities.Alarm

@Dao
interface AlarmDao {
    @Insert
    fun insert(alarm: Alarm)

    @Query("DELETE FROM alarm_table")
    fun deleteAll()

    @get:Query("SELECT * FROM alarm_table ORDER BY created ASC")
    val alarms: LiveData<List<Alarm>>

    @Update
    fun update(alarm: Alarm)
}

