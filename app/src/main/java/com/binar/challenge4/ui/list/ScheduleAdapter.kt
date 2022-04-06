package com.binar.challenge4.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.binar.challenge4.data.Schedule
import com.binar.challenge4.databinding.ScheduleListLayoutBinding

class ScheduleAdapter(private val delClick:(Schedule)->Unit,
                      private val editClick:(Schedule)-> Unit)
    : ListAdapter<Schedule, ScheduleAdapter.ViewHolder>(ScheduleComparator()) {



    class ViewHolder(private val binding: ScheduleListLayoutBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(currentSchedule: Schedule,
                 delClick: (Schedule) -> Unit,
                 editClick: (Schedule) -> Unit){

            binding.apply {
                textView.text = currentSchedule.event_type
                binding.root.setOnClickListener {
                    delClick(currentSchedule)
                }
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

    class ScheduleComparator : DiffUtil.ItemCallback<Schedule>() {
        override fun areItemsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ScheduleListLayoutBinding.inflate(
            LayoutInflater.from(parent.context),parent,false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), delClick, editClick)
    }

}