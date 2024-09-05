package com.example.customalarm.presentation.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.customalarm.R
import com.example.customalarm.data.local.entities.Alarm
import com.example.customalarm.databinding.ItemAlarmBinding
import com.example.customalarm.presentation.ui.listeners.OnToggleAlarmListener
import kotlin.text.*


class AlarmListAdapter(listener: OnToggleAlarmListener) :
    RecyclerView.Adapter<AlarmListAdapter.AlarmViewHolder>() {
    private var alarms: List<Alarm>
    private val listener: OnToggleAlarmListener

    class AlarmViewHolder(val binding: ItemAlarmBinding) :
        RecyclerView.ViewHolder(binding.root)


    init {
        this.alarms = ArrayList()
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        return AlarmViewHolder(
            ItemAlarmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        val alarm: Alarm = alarms[position]
        val alarmText = "${alarm.hour.toString().padStart(2, '0')}:${alarm.minute.toString().padStart(2, '0')}"


        holder.binding.itemAlarmTime.text = alarmText
        holder.binding.itemAlarmStarted.isChecked = alarm.isStarted

        if (alarm.isRecurring) {
            holder.binding.itemAlarmRecurring.setImageResource(R.drawable.ic_repeat_black_24dp)
            holder.binding.itemAlarmRecurringDays.text = alarm.recurringDaysText ?: "Once Off"
        } else {
            holder.binding.itemAlarmRecurring.setImageResource(R.drawable.ic_looks_one_black_24dp)
            holder.binding.itemAlarmRecurringDays.text = "Once Off"
        }

        holder.binding.itemAlarmStarted.setOnCheckedChangeListener { _, _ ->
            listener.onToggle(alarm)
        }
    }


    override fun getItemCount(): Int {
        return alarms.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setAlarms(alarms: List<Alarm>) {
        this.alarms = alarms
        notifyDataSetChanged()
    }

    override fun onViewRecycled(holder: AlarmViewHolder) {
        super.onViewRecycled(holder)
        holder.binding.itemAlarmStarted.setOnCheckedChangeListener(null)
    }
}
