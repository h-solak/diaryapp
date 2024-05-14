package com.hasansolak.diaryapp.fragments

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.hasansolak.diaryapp.MainActivity
import com.hasansolak.diaryapp.R
import com.hasansolak.diaryapp.databinding.FragmentEditDiaryBinding
import com.hasansolak.diaryapp.model.Diary
import com.hasansolak.diaryapp.viewmodel.DiaryViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.*

class EditDiaryFragment : Fragment(R.layout.fragment_edit_diary), MenuProvider {

    private var editDiaryBinding: FragmentEditDiaryBinding? = null
    private val binding get() = editDiaryBinding!!

    private lateinit var diariesViewModel: DiaryViewModel
    private lateinit var currentDiary: Diary

    private val args: EditDiaryFragmentArgs by navArgs()

    private val calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        editDiaryBinding = FragmentEditDiaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        diariesViewModel = (activity as MainActivity).diaryViewModel
        currentDiary = args.diary!!

        binding.btnEditDatePicker.text = currentDiary.diaryDate
        binding.editDiaryContent.setText(currentDiary.diaryContent)

        binding.btnEditDatePicker.setOnClickListener {
            showDatePicker()
        }

        //update diary
        binding.editDiaryBtn.setOnClickListener {
            val diaryDate = binding.btnEditDatePicker.text.toString().trim()
            val diaryContent = binding.editDiaryContent.text.toString().trim()

            if (diaryDate.isNotEmpty() && diaryContent.isNotEmpty()){
                val diary = Diary(currentDiary.id, diaryDate, diaryContent)
                diariesViewModel.updateDiary(diary)
                view.findNavController().popBackStack(R.id.homeFragment, false)
            } else {
                Toast.makeText(context, "Something must have happened this day!", Toast.LENGTH_SHORT).show()
            }
        }
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
                binding.btnEditDatePicker.text = "$formattedDate"

            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        // Show the DatePicker dialog
        datePickerDialog.show()
    }

    private fun deleteDiary(){
        AlertDialog.Builder(activity).apply {
            setTitle("Delete Diary")
            setMessage("You can't undo this action. Do you want to delete this diary? ")
            setPositiveButton("Delete"){_,_ ->
                diariesViewModel.deleteDiary(currentDiary)
                Toast.makeText(context, " Diary Deleted", Toast.LENGTH_SHORT).show()
                view?.findNavController()?.popBackStack(R.id.homeFragment, false)
            }
            setNegativeButton("Cancel", null)
        }.create().show()
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.menu_edit_diary, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when(menuItem.itemId){
            R.id.deleteMenu -> {
                deleteDiary()
                true
            } else -> false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        editDiaryBinding = null
    }
}