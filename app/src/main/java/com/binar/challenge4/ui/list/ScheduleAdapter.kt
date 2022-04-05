package com.binar.challenge4.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.binar.challenge4.data.Schedule
import com.binar.challenge4.databinding.ScheduleListLayoutBinding

class ScheduleAdapter(private val schedules: List<Schedule>,
                     private val delClick:(Schedule)->Unit,
                     private val editClick:(Schedule)-> Unit)
    : RecyclerView.Adapter<ScheduleAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ScheduleListLayoutBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(currentSchedule: Schedule,
                 delClick: (Schedule) -> Unit,
                 editClick: (Schedule) -> Unit){

            binding.apply {
                textView.text = currentSchedule.event_type
//                tvNama.text = currentSchedule.nama
//                tvEmail.text = currentSchedule.email
//
//                btnEdit.setOnClickListener {
//                    editClick(currentSchedule)
//                }
//
//                btnDelete.setOnClickListener {
//                    delClick(currentSchedule)
//                }
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ScheduleListLayoutBinding.inflate(
            LayoutInflater.from(parent.context),parent,false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(schedules[position], delClick, editClick)
    }

    override fun getItemCount(): Int = schedules.size
}