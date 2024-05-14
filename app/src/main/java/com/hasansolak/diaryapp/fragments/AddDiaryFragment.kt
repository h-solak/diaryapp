package com.hasansolak.diaryapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import com.hasansolak.diaryapp.MainActivity
import com.hasansolak.diaryapp.R
import com.hasansolak.diaryapp.databinding.FragmentAddDiaryBinding
import com.hasansolak.diaryapp.model.Diary
import com.hasansolak.diaryapp.viewmodel.DiaryViewModel
import java.util.Calendar
import android.app.DatePickerDialog
import android.widget.DatePicker
import java.text.SimpleDateFormat
import java.util.*

class AddDiaryFragment : Fragment(R.layout.fragment_add_diary), MenuProvider {

    private var addDiaryBinding: FragmentAddDiaryBinding? = null
    private val binding get() = addDiaryBinding!!

    private lateinit var diariesViewModel: DiaryViewModel
    private lateinit var addDiaryView: View

    private val calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        addDiaryBinding = FragmentAddDiaryBinding.inflate(inflater, container, false)

        binding.btnDatePicker.setOnClickListener {
           showDatePicker()
        }

        //set todays date when view is started (create mode)
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        val todayDate = "${String.format("%02d", dayOfMonth)}/${String.format("%02d", month + 1)}/${year}"
        binding.btnDatePicker.text = todayDate

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        diariesViewModel = (activity as MainActivity).diaryViewModel
        addDiaryView = view
    }

    private fun showDatePicker() {
        // Create a DatePickerDialog using requireContext() to get the context
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                // Create a new Calendar instance to hold the selected date
                val selectedDate = Calendar.getInstance()
                // Set the selected date using the values received from the DatePicker dialog
                selectedDate.set(year, monthOfYear, dayOfMonth)
                // Create a SimpleDateFormat to format the date as "dd/MM/yyyy"
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                // Format the selected date into a string
                val formattedDate = dateFormat.format(selectedDate.time)
                // Update the TextView to display the selected date with the "Selected Date: " prefix
                //binding.tvSelectedDate.text = "Selected Date: $formattedDate"
                binding.btnDatePicker.text = "$formattedDate"

            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        // Show the DatePicker dialog
        datePickerDialog.show()
    }

    private fun saveDiary(view: View){
        val diaryDate = binding.btnDatePicker.text.toString().trim()
        val diaryContent = binding.addDiaryContent.text.toString().trim()

        if (diaryDate.isNotEmpty()){
            val diary = Diary(0, diaryDate, diaryContent)
            diariesViewModel.createDiary(diary)

            Toast.makeText(addDiaryView.context, "Your diary is saved successfully!", Toast.LENGTH_SHORT).show()
            view.findNavController().popBackStack(R.id.homeFragment, false)
        } else {
            Toast.makeText(addDiaryView.context, "You need to enter a date!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.menu_add_diary, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when(menuItem.itemId){
            R.id.saveMenu -> {
                saveDiary(addDiaryView)
                true
            }
            else -> false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        addDiaryBinding = null
    }
}