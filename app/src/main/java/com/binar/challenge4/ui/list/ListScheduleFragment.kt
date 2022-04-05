package com.binar.challenge4.ui.list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.binar.challenge4.MainActivity
import com.binar.challenge4.R
import com.binar.challenge4.data.Schedule
import com.binar.challenge4.database.MyDatabase
import com.binar.challenge4.databinding.FragmentListScheduleBinding
import com.binar.challenge4.databinding.FragmentRegisterBinding
import com.binar.challenge4.ui.form.FormFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class ListScheduleFragment : Fragment() {

    private var _binding: FragmentListScheduleBinding? = null
    private var myDatabase: MyDatabase? = null
    private val binding get() = _binding!!

    private var adapter:ScheduleAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentListScheduleBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        fetchData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myDatabase = MyDatabase.getInstance(requireContext())

        val sharedPreference = requireContext()
            .getSharedPreferences(MainActivity.SHARED_FILE, Context.MODE_PRIVATE)

//        binding.button.setOnClickListener {
//            val editor = sharedPreference.edit()
//            editor.clear()
//            editor.apply()
//        }

        fetchStudent()

        binding.floatingActionButton.setOnClickListener {

            val dialog = FormFragment{
                lifecycleScope.launch(Dispatchers.IO) {
                    val result = myDatabase?.scheduleDao()?.insertSchedule(it)
                    activity?.runOnUiThread {
                        if (result == (0).toLong()){
                            Toast.makeText(context,
                                "gagal insert", Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(context,
                                "sukses insert", Toast.LENGTH_SHORT).show()
                            fetchStudent()
                        }
                    }
                }
            }

            dialog.show(parentFragmentManager,"dialog")

//            val action = ListScheduleFragmentDirections.actionListScheduleFragmentToFormFragment2()
//            it.findNavController().navigate(action)
        }


    }

    private fun fetchStudent(){
        fetchData()
//        binding.floatingActionButton.setOnClickListener {
//            val intent = Intent(this, FormActivity::class.java)
//            startActivity(intent)
//        }
    }

    private fun fetchData(){
        lifecycleScope.launch(Dispatchers.IO) {
            val myDb = myDatabase?.scheduleDao()
            val listStudent = myDb?.getAllSchedule()

            activity?.runOnUiThread {
                listStudent?.let {
                    adapter = ScheduleAdapter(it, {}, {})
                    binding.rvSchedule.adapter = adapter
                }
            }

        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}