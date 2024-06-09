package com.sandeepprabhakula.budgetapp.fragments.add

import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.sandeepprabhakula.budgetapp.R
import com.sandeepprabhakula.budgetapp.entities.DailyBudgetEntity
import com.sandeepprabhakula.budgetapp.viewModel.BudgetViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit

class AddTodayBudgetFragment : Fragment() {
    private lateinit var viewModel: BudgetViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_today_budget, container, false)
        val selectedDateTextView = view.findViewById<TextView>(R.id.selectedDateTextView)
        val calender: CalendarView = view.findViewById(R.id.calendarView)
        selectedDateTextView.text = convertDate(calender.date)
        calender.setOnDateChangeListener { calendarView, year, i2, dayOfMonth ->
            var month = ""
            var day=""
            if(i2+1<10)month = "0${i2+1}"
            else month+=i2+1
            if(dayOfMonth<10)day="0$dayOfMonth"
            else day+=dayOfMonth
            selectedDateTextView.text="$day-$month-$year"
            calendarView.date = Calendar.getInstance().apply {
                set(year, i2, dayOfMonth)
            }.timeInMillis
        }
        val exp: EditText = view.findViewById(R.id.expenditure)
        val button: Button = view.findViewById(R.id.addBudget)
        viewModel = ViewModelProvider(this)[BudgetViewModel::class.java]

        button.setOnClickListener {

            val dateStr = selectedDateTextView.text.toString()
            val expText = exp.text.toString()
            if (!TextUtils.isEmpty(dateStr) || !TextUtils.isEmpty(expText)) {
                val budget = DailyBudgetEntity(0, dateStr, expText)
                viewModel.addTodayBudget(budget)
                Toast.makeText(requireContext(), "added Successfully", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_addTodayBudgetFragment_to_allBudgetListFragment)
            } else {
                Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }
    private fun convertDate(millis: Long): String {
        val sdf = SimpleDateFormat("dd-MM-yyyy")
        val date = Date(TimeUnit.MILLISECONDS.toMillis(millis))
        return sdf.format(date)
    }
}