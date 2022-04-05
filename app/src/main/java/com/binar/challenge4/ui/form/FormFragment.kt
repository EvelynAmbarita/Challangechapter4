package com.binar.challenge4.ui.form

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.binar.challenge4.R
import com.binar.challenge4.data.Schedule
import com.binar.challenge4.database.MyDatabase
import com.binar.challenge4.databinding.FragmentFormBinding
import com.binar.challenge4.databinding.FragmentListScheduleBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class FormFragment : Fragment() {

    private var _binding: FragmentFormBinding? = null
    private var myDatabase: MyDatabase? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFormBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        myDatabase = MyDatabase.getInstance(requireContext())

//        val schedule: Schedule? = intent.getParcelableExtra("schedule")

        addData()
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
                lifecycleScope.launch(Dispatchers.IO) {
                    val result = myDatabase?.scheduleDao()?.insertSchedule(schedule)
                    activity?.runOnUiThread {
                        if (result == (0).toLong()){
                            Toast.makeText(context,
                                "gagal insert", Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(context,
                                "sukses insert", Toast.LENGTH_SHORT).show()
                        }
                        findNavController().popBackStack()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}