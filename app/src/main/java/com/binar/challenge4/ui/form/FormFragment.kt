package com.binar.challenge4.ui.form

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.binar.challenge4.R
import com.binar.challenge4.data.Schedule
import com.binar.challenge4.database.MyDatabase
import com.binar.challenge4.databinding.FragmentFormBinding
import com.binar.challenge4.databinding.FragmentListScheduleBinding
import com.binar.challenge4.ui.list.ScheduleAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.IllegalStateException


class FormFragment(private val saveClick:(Schedule)->Unit) : DialogFragment() {

    private var _binding: FragmentFormBinding? = null
    private var myDatabase: MyDatabase? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!



    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        myDatabase = MyDatabase.getInstance(requireContext())
        _binding = FragmentFormBinding.inflate(layoutInflater)
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setView(binding.root)
            binding.apply {
                btnSave.setOnClickListener {
                    val schedule = Schedule(null, etNama.text.toString())
                    saveClick(schedule)
                    dialog?.dismiss()
                }
            }
            builder.create()
        }?:throw IllegalStateException("Activity cannot be null")

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




//        val schedule: Schedule? = intent.getParcelableExtra("schedule")


//        if (schedule ==null){
//            //do add
//            addData()
//        }else{
//            //do edit
//            editData(schedule)
//        }

    }

    private fun editData(schedule: Schedule){
//        binding.apply {
//            etNama.setText(schedule.nama)
//            etEmail.setText(schedule.email)
//
//            btnSave.setOnClickListener {
//                val newSchedule = Schedule(
//                    schedule.id,
//                    etNama.text.toString(),
//                    etEmail.text.toString()
//                )
//                GlobalScope.async {
//                    val result = myDatabase?.scheduleDao()?.updateSchedule(newSchedule)
//
//                    runOnUiThread {
//                        if (result == 0){
//                            Toast.makeText(context,
//                                "gagal update", Toast.LENGTH_SHORT).show()
//                        }else{
//                            Toast.makeText(context,
//                                "sukses update", Toast.LENGTH_SHORT).show()
//                        }
//                        finish()
//
//
//                    }
//
//                }
//            }
//
//        }
    }

    private fun addData(){
        binding.apply {
            btnSave.setOnClickListener {
                val schedule = Schedule(null, etNama.text.toString())
                saveClick(schedule)
//                val schedule = Schedule(null, etNama.text.toString())
//                lifecycleScope.launch(Dispatchers.IO) {
//                    val result = myDatabase?.scheduleDao()?.insertSchedule(schedule)
//                    activity?.runOnUiThread {
//                        if (result == (0).toLong()){
//                            Toast.makeText(context,
//                                "gagal insert", Toast.LENGTH_SHORT).show()
//                        }else{
//                            Toast.makeText(context,
//                                "sukses insert", Toast.LENGTH_SHORT).show()
//                        }
//                        findNavController().popBackStack()
//                    }
//                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}