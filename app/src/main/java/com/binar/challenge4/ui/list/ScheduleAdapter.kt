package com.binar.challenge4.ui.list

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.binar.challenge4.R
import com.binar.challenge4.data.Schedule
import com.binar.challenge4.databinding.ScheduleListLayoutBinding
import com.binar.challenge4.ui.calendar.CalendarFragment
import com.binar.challenge4.utils.EventTimeConverter
import com.bumptech.glide.Glide
import java.time.Instant
import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit
import java.util.*

class ScheduleAdapter(private val delClick:(Schedule)->Unit,
                      private val editClick:(Schedule)-> Unit)
    : ListAdapter<Schedule, ScheduleAdapter.ViewHolder>(ScheduleComparator()) {


    class ViewHolder(private val binding: ScheduleListLayoutBinding): RecyclerView.ViewHolder(binding.root) {

        lateinit var getSchedule: Schedule
        var currentPosition = -1

        fun bind(currentSchedule: Schedule,
                 delClick: (Schedule) -> Unit,
                 editClick: (Schedule) -> Unit){

            getSchedule = currentSchedule

            val cal = Calendar.getInstance()
            cal.add(Calendar.DATE,-1)
            val yesterday = cal.timeInMillis




//            val now = Instant.now()
//            now.minus(1, ChronoUnit.DAYS)

            binding.apply {
                var formatDate = ""
                val rawDate = currentSchedule.eventDate
                rawDate.split("/").forEachIndexed {index, s ->
                    formatDate += if (index !=2){
                        "$s\n"
                    }else{
                        s
                    }
                }
                tvTglEvent.text = formatDate

                if (currentSchedule.eventDateMillis<=yesterday){

//                    val strike = binding.root.context.getString(R.string.outdated_schedule,currentSchedule.eventDate)
//                    tvTglEvent.text = strike
                    tvTglEvent.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    ivDoneMark.visibility = View.VISIBLE

                }

                when(currentSchedule.eventType){
                    "Wedding" -> Glide.with(binding.root.context)
                        .load(R.drawable.couple)
                        .into(binding.ivIconList)
                    "Graduation" -> Glide.with(binding.root.context)
                        .load(R.drawable.graduation_hat)
                        .into(binding.ivIconList)
                    "Seminar" -> Glide.with(binding.root.context)
                        .load(R.drawable.seminar)
                        .into(binding.ivIconList)
                    "Ceremony" -> Glide.with(binding.root.context)
                        .load(R.drawable.red_carpet)
                        .into(binding.ivIconList)
                    else -> Glide.with(binding.root.context)
                        .load(R.drawable.document)
                        .into(binding.ivIconList)
                }

                if (currentSchedule.description.isEmpty()){
                    binding.tvNote.visibility = View.GONE
                }else{
                    binding.tvNote.text = "Note:\n${currentSchedule.description}"
                }




                tvEventType.text = currentSchedule.eventType

                val eventTime = EventTimeConverter.convert(currentSchedule.choosenTime)

                tvEventTime.text = eventTime

                tvNote.text = currentSchedule.description

                tvEventTime
                binding.btnEdit.setOnClickListener {
                    editClick(currentSchedule)
                }
                binding.btnDelete.setOnClickListener {
                    delClick(currentSchedule)
                }
            }

        }

    }

    class ScheduleComparator : DiffUtil.ItemCallback<Schedule>() {
        override fun areItemsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
            return oldItem.id === newItem.id
        }

        override fun areContentsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ScheduleListLayoutBinding.inflate(
            LayoutInflater.from(parent.context),parent,false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.getSchedule = getItem(position)
        holder.currentPosition = position
        holder.bind(getItem(position), delClick, editClick)
    }

}