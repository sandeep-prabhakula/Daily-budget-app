package com.sandeepprabhakula.budgetapp.fragments.add

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.sandeepprabhakula.budgetapp.viewModel.BudgetViewModel
import com.sandeepprabhakula.budgetapp.entities.DailyBudgetEntity
import com.sandeepprabhakula.budgetapp.R

class AddTodayBudgetFragment : Fragment() {
    private lateinit var viewModel: BudgetViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_today_budget, container, false)

        val date:EditText = view.findViewById(R.id.todaysDate)
        val exp:EditText = view.findViewById(R.id.expenditure)
        val button:Button = view.findViewById(R.id.addBudget)
        viewModel = ViewModelProvider(this).get(BudgetViewModel::class.java)
        button.setOnClickListener {
            val dateText = date.text.toString()
            val expText = exp.text.toString()
            if(!TextUtils.isEmpty(dateText)||!TextUtils.isEmpty(expText)){
                val budget = DailyBudgetEntity(0,dateText,expText)
                viewModel.addTodayBudget(budget)
                Toast.makeText(requireContext(),"added Successfully",Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_addTodayBudgetFragment_to_allBudgetListFragment)
            }else{
                Toast.makeText(requireContext(),"Something went wrong",Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }
}