package com.binar.challenge4.ui.form

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.view.get
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.binar.challenge4.R
import com.binar.challenge4.data.Schedule
import com.binar.challenge4.database.MyDatabase
import com.binar.challenge4.databinding.FragmentFormBinding
import com.binar.challenge4.databinding.FragmentListScheduleBinding
import com.binar.challenge4.ui.calendar.CalendarFragment
import com.binar.challenge4.ui.list.ScheduleAdapter
import com.binar.challenge4.utils.DateConverter
import com.binar.challenge4.utils.SetIdSpinner
import com.binar.challenge4.utils.ValidationForm
import com.binar.challenge4.utils.ValidationForm.isValidOnly
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.IllegalStateException
import java.util.*
import kotlin.collections.ArrayList


class FormFragment(private val schedule: Schedule?): BottomSheetDialogFragment() {

    var saveClick:(Schedule)->Unit = {}
    var editClick:(Schedule)->Unit = {}

    private val choosenTime = ArrayList<Int>()

    private var _binding: FragmentFormBinding? = null
    private var myDatabase: MyDatabase? = null
    private val binding get() = _binding!!
    private var dateMillis = System.currentTimeMillis()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFormBinding.inflate(inflater,container,false)
        return binding.root

    }


//    val builder = BottomSheetDialog(requireContext(),R.style.SheetDialog)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        myDatabase = MyDatabase.getInstance(requireContext())
        _binding = FragmentFormBinding.inflate(layoutInflater)

        val bottomSheet = BottomSheetDialog(requireContext(),R.style.SheetDialog)

        bottomSheet.setContentView(binding.root)

        bottomSheet.behavior.state = BottomSheetBehavior.STATE_EXPANDED

        return bottomSheet


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            etTglEvent.isEnabled = false
            cbMorning.isEnabled = false
            cbAfternoon.isEnabled = false
            cbNight.isEnabled = false


            etTglEvent.addTextChangedListener {
                cbMorning.isEnabled = true
                cbAfternoon.isEnabled = true
                cbNight.isEnabled = true

            }
        }


        binding.btnCalendar.setOnClickListener {
            val calendarFragment = CalendarFragment(System.currentTimeMillis(), dateMillis){
                dateMillis = it
                val dateStr = DateConverter.convertMillisToString(it)
                binding.etTglEvent.setText(dateStr)
            }
            calendarFragment.show(parentFragmentManager, "datePicker")
        }

        binding.spJenisAcara.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                if (p2!=5){
                    binding.etJenisAcara.visibility = View.GONE
                }else{
                    binding.etJenisAcara.visibility = View.VISIBLE
                }

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}

        }

        binding.cbMorning.setOnCheckedChangeListener { compoundButton, b ->
            if (b){
                choosenTime.add(1)
            }else{
                choosenTime.remove(1)
            }
        }

        binding.cbAfternoon.setOnCheckedChangeListener { compoundButton, b ->
            if (b){
                choosenTime.add(2)
            }else{
                choosenTime.remove(2)
            }
        }

        binding.cbNight.setOnCheckedChangeListener { compoundButton, b ->
            if (b){
                choosenTime.add(3)
            }else{
                choosenTime.remove(3)
            }
        }

        schedule?.let {
            //do edit

            editData(it)

        }?: kotlin.run {
            addData()
        }

    }

    private fun editData(schedule: Schedule){
        binding.apply {

            btnCalendar.setOnClickListener {
                val calendarFragment = CalendarFragment(0, dateMillis){
                    dateMillis = it
                    val dateStr = DateConverter.convertMillisToString(it)
                    binding.etTglEvent.setText(dateStr)
                }
                calendarFragment.show(parentFragmentManager, "datePicker")
            }

            var jenisAcara = ""
            val idSpinner = SetIdSpinner.setIdSpinner(schedule.eventType)
            spJenisAcara.setSelection(idSpinner)

            if (idSpinner == 5){
                etJenisAcara.setText(schedule.eventType)
            }

            val editChoosenTime = schedule.choosenTime.split(",").map {
                it.trim()
            }
            cbMorning.isChecked = editChoosenTime.contains("1")
            cbAfternoon.isChecked = editChoosenTime.contains("2")
            cbNight.isChecked = editChoosenTime.contains("3")
            Log.i("splitted", editChoosenTime.toString())

            dateMillis = schedule.eventDateMillis

            etTglEvent.addTextChangedListener {

                if (it.toString() != schedule.eventDate){
                    cbMorning.isChecked = false
                    cbAfternoon.isChecked = false
                    cbNight.isChecked = false
                }else{
                    cbMorning.isChecked = editChoosenTime.contains("1")
                    cbAfternoon.isChecked = editChoosenTime.contains("2")
                    cbNight.isChecked = editChoosenTime.contains("3")
                }

                val availabelChoosenTime = ArrayList<String>()
                availabelChoosenTime.add("1")
                availabelChoosenTime.add("2")
                availabelChoosenTime.add("3")

                lifecycleScope.launch(Dispatchers.IO){
                    val selectedDateList = myDatabase?.scheduleDao()
                        ?.getSelectedDaySchedule(binding.etTglEvent.text.toString())
                    activity?.runOnUiThread {
                        selectedDateList?.forEach { currentDate ->
                            if (currentDate.id != schedule.id){

                                val selectedChoosenTime = currentDate.choosenTime.split(",").map {
                                    it.trim()
                                }
                                selectedChoosenTime.forEach {
                                    Log.i("testremoved", it)
                                    availabelChoosenTime.remove(it)
                                    Log.i("testremove",availabelChoosenTime.toString())
                                }
                            }
                        }

                        cbMorning.isEnabled = availabelChoosenTime.contains("1")
                        cbAfternoon.isEnabled = availabelChoosenTime.contains("2")
                        cbNight.isEnabled = availabelChoosenTime.contains("3")

                    }
                }
            }
            etTglEvent.setText(schedule.eventDate)
            etDeskripsi.setText(schedule.description)

            btnSave.setOnClickListener {

                if (ValidationForm.eventTypeValid(binding.spJenisAcara, binding.etJenisAcara) and
                    (binding.cbAfternoon.isValidOnly() or
                            binding.cbMorning.isValidOnly() or
                            binding.cbNight.isValidOnly())){

                    jenisAcara = if (idSpinner==5){
                        etJenisAcara.text.toString()
                    }else{
                        spJenisAcara.selectedItem.toString()
                    }

                    val newSchedule =
                        Schedule(schedule.id,
                            jenisAcara,
                            binding.etTglEvent.text.toString(),
                            dateMillis,
                            choosenTime.sorted().joinToString(),
                            binding.etDeskripsi.text.toString())
                    editClick(newSchedule)
                    dismiss()

                }else{
                    Toast.makeText(context, "Harap isi dengan lengkap", Toast.LENGTH_SHORT).show()
                }



            }

        }
    }

    private fun addData(){

        binding.btnCalendar.setOnClickListener {
            val calendarFragment = CalendarFragment(System.currentTimeMillis(), dateMillis){
                dateMillis = it
                val dateStr = DateConverter.convertMillisToString(it)
                binding.etTglEvent.setText(dateStr)
            }
            calendarFragment.show(parentFragmentManager, "datePicker")
        }

        binding.etTglEvent.addTextChangedListener {

            val availabelChoosenTime = ArrayList<String>()
            availabelChoosenTime.addAll(arrayListOf("1","2","3"))

            lifecycleScope.launch(Dispatchers.IO){
                val selectedDateList = myDatabase?.scheduleDao()
                    ?.getSelectedDaySchedule(binding.etTglEvent.text.toString())
                activity?.runOnUiThread {
                    selectedDateList?.forEach { currentDate ->
                        val selectedChoosenTime = currentDate.choosenTime.split(",").map {
                            it.trim()
                        }
                        selectedChoosenTime.forEach {
                            availabelChoosenTime.remove(it)
                        }

                    }

                    if (availabelChoosenTime.size == 0){
//                    Snackbar.make(binding.root,"Jadwal Hari ini telah habis!",Snackbar.LENGTH_INDEFINITE)
//                        .setAction("OK") {
//                            dismiss()
//                        }
//                        .show()

                        Toast.makeText(context, "Jadwal untuk tanggal ${binding.etTglEvent.text} habis!", Toast.LENGTH_SHORT).show()

                    }

                    Log.i("testsekian", availabelChoosenTime.toString())

                    binding.cbMorning.isEnabled = availabelChoosenTime.contains("1")
                    binding.cbAfternoon.isEnabled = availabelChoosenTime.contains("2")
                    binding.cbNight.isEnabled = availabelChoosenTime.contains("3")

                }
            }



        }



        binding.btnSave.setOnClickListener {

            if (ValidationForm.eventTypeValid(binding.spJenisAcara, binding.etJenisAcara) and
                (binding.cbAfternoon.isValidOnly() or
                    binding.cbMorning.isValidOnly() or
                    binding.cbNight.isValidOnly())){

                val idSpinner = binding.spJenisAcara.selectedItemPosition

                val jenisAcara = if (idSpinner==5){
                    binding.etJenisAcara.text.toString()
                }else{
                    binding.spJenisAcara.selectedItem.toString()
                }

                val schedule =
                    Schedule(null,
                        jenisAcara,
                        binding.etTglEvent.text.toString(),
                        dateMillis,
                        choosenTime.sorted().joinToString(),
                        binding.etDeskripsi.text.toString())
                saveClick(schedule)
                dismiss()

            }else{
                Toast.makeText(context, "Harap isi dengan lengkap", Toast.LENGTH_SHORT).show()
            }


        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}