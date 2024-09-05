package com.example.customalarm.data.local.entities
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarm_table")
data class Alarm(
    @PrimaryKey var alarmId: Int,
    val hour: Int,
    val minute: Int,
    var created: Long,
    var isStarted: Boolean,
    val isRecurring: Boolean,
    val isMonday: Boolean,
    val isTuesday: Boolean,
    val isWednesday: Boolean,
    val isThursday: Boolean,
    val isFriday: Boolean,
    val isSaturday: Boolean,
    val isSunday: Boolean
){
    val recurringDaysText: String?
        get() {
            if (!isRecurring) {
                return null
            }

            val days = mutableListOf<String>()
            if (isMonday) days.add("Mo")
            if (isTuesday) days.add("Tu")
            if (isWednesday) days.add("We")
            if (isThursday) days.add("Th")
            if (isFriday) days.add("Fr")
            if (isSaturday) days.add("Sa")
            if (isSunday) days.add("Su")

            return days.joinToString(" ")
        }
}

