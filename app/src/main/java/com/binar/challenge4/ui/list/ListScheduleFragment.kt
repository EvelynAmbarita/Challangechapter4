package com.binar.challenge4.ui.list

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.edit
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.binar.challenge4.MainActivity
import com.binar.challenge4.R
import com.binar.challenge4.data.Schedule
import com.binar.challenge4.database.MyDatabase
import com.binar.challenge4.databinding.FragmentListScheduleBinding
import com.binar.challenge4.databinding.FragmentRegisterBinding
import com.binar.challenge4.ui.form.FormFragment
import com.binar.challenge4.ui.repository.ScheduleRepository
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class ListScheduleFragment : Fragment() {

    lateinit var scheduleRepository: ScheduleRepository
    private var _binding: FragmentListScheduleBinding? = null
//    private var myDatabase: MyDatabase? = null
    private val binding get() = _binding!!

    private lateinit var adapter:ScheduleAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentListScheduleBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        scheduleRepository = ScheduleRepository(requireContext())
//        myDatabase = MyDatabase.getInstance(requireContext())



        val sharedPreference = requireContext()
            .getSharedPreferences(MainActivity.SHARED_FILE, Context.MODE_PRIVATE)
        val nameLogin = sharedPreference.getString("name","")
        binding.tvNama.text = "Welcome,\n$nameLogin!"

        binding.ivLogOut.setOnClickListener{
            sharedPreference.edit {
                this.clear()
                this.apply()
            }

            val action = ListScheduleFragmentDirections.actionListScheduleFragmentToLoginFragment()
            it.findNavController().navigate(action)

        }

        initAction()

        adapter = ScheduleAdapter(
            delClick = { schedule ->

                val confirmDialog = AlertDialog.Builder(requireContext())
                confirmDialog.setTitle("Konfirmasi Hapus")
                confirmDialog.setMessage("Apakah anda yakin inigin menghapus schedule ini?")
                confirmDialog.setPositiveButton("OK",DialogInterface.OnClickListener{dialogInterface, i ->
                    deleteData(schedule)
                    val snackbar = Snackbar.make(binding.root,"Berhasil Delete!", Snackbar.LENGTH_LONG)
                    snackbar.setAction("OK") {
                        snackbar.dismiss()
                    }.show()
                })
                confirmDialog.setNegativeButton("Cancel") { dialogInterface, i ->
                    dialogInterface.dismiss()
                }
                confirmDialog.create().show()


            },
            editClick = {schedule ->
                val dialog = FormFragment(schedule)
                dialog.editClick = {
                    lifecycleScope.launch(Dispatchers.IO) {
//                        val result = myDatabase?.scheduleDao()?.updateSchedule(it)
                        val result = scheduleRepository.updateSchedule(it)
                        activity?.runOnUiThread {
                            if (result == (0)){
                                Toast.makeText(context,
                                    "gagal update", Toast.LENGTH_SHORT).show()
                            }else{
                                Toast.makeText(context,
                                    "sukses update", Toast.LENGTH_SHORT).show()
                                fetchData()
                            }
                        }
                    }
                }
                dialog.show(parentFragmentManager,"dialogedit")
            })
        binding.rvSchedule.adapter = adapter

        fetchSchedule()

        binding.floatingActionButton.setOnClickListener {

            val dialog = FormFragment(null)

            dialog.saveClick = {
                lifecycleScope.launch(Dispatchers.IO) {
//                    val result = myDatabase?.scheduleDao()?.insertSchedule(it)
                    val result = scheduleRepository.addSchedule(it)
                    activity?.runOnUiThread {
                        if (result == (0).toLong()){
                            Toast.makeText(context,
                                "gagal insert", Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(context,
                                "sukses insert", Toast.LENGTH_SHORT).show()
                            fetchData()
                        }
                    }
                }
            }

            dialog.show(parentFragmentManager,"dialogadd")

        }


    }

    private fun fetchSchedule(){
        fetchData()
    }

    private fun fetchData(){
        lifecycleScope.launch(Dispatchers.IO) {
//            val myDb = myDatabase?.scheduleDao()
//            val listStudent = myDb?.getAllSchedule()
            val listStudent = scheduleRepository.getAllSchedule()

            activity?.runOnUiThread {
                listStudent?.let {
                    adapter.submitList(it)
                }
                if (listStudent?.size==0){
                    binding.tvEmptyList.visibility = View.VISIBLE
                }else{
                    binding.tvEmptyList.visibility = View.GONE
                }

//                val spinnerAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item)
//                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                spinner.setAdapter(spinnerAdapter)

            }

        }
    }

    private fun initAction() {
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                return makeMovementFlags(0, ItemTouchHelper.RIGHT)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {


                val schedule = (viewHolder as ScheduleAdapter.ViewHolder).getSchedule
                val confirmDialog = AlertDialog.Builder(requireContext())
                confirmDialog.setTitle("Konfirmasi Hapus")
                confirmDialog.setMessage("Apakah anda yakin inigin menghapus schedule ini?")
                confirmDialog.setPositiveButton("OK",DialogInterface.OnClickListener{dialogInterface, i ->


                    deleteData(schedule)

                    val snackbar = Snackbar.make(binding.root,"Berhasil Delete!", Snackbar.LENGTH_LONG)

                    snackbar.setAction("OK") {
                        snackbar.dismiss()
                    }
                    snackbar.show()


                })
                confirmDialog.setNegativeButton("Cancel") { dialogInterface, i ->
                    dialogInterface.dismiss()
                    adapter.notifyItemChanged(viewHolder.adapterPosition)
                }
                confirmDialog.create().show()
            }

        })
        itemTouchHelper.attachToRecyclerView(binding.rvSchedule)
    }

    private fun deleteData(schedule: Schedule){
        lifecycleScope.launch(Dispatchers.IO) {
//            val result = myDatabase?.scheduleDao()?.deleteSchedule(schedule)
            val result = scheduleRepository.deleteSchedule(schedule)

            activity?.runOnUiThread {
                if (result == 0) {
                    Toast
                        .makeText(
                            context,
                            "gagal delete",
                            Toast.LENGTH_SHORT
                        )
                        .show()
                }
                fetchData()
            }

        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}