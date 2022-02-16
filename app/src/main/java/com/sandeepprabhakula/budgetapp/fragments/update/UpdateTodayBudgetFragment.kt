package com.sandeepprabhakula.budgetapp.fragments.update

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.sandeepprabhakula.budgetapp.R
import com.sandeepprabhakula.budgetapp.entities.DailyBudgetEntity
import com.sandeepprabhakula.budgetapp.fragments.read.AllBudgetListFragmentDirections
import com.sandeepprabhakula.budgetapp.viewModel.BudgetViewModel

class UpdateTodayBudgetFragment : Fragment() {
    private val args by navArgs<UpdateTodayBudgetFragmentArgs>()
    private lateinit var viewModel: BudgetViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_update_today_budget, container, false)
        val dateUP:EditText = view.findViewById(R.id.todaysDateUP)
        val expUP:EditText = view.findViewById(R.id.expenditureUP)
        val updateBudget:Button = view.findViewById(R.id.updateBudget)
        dateUP.setText(args.currentBudget.date)
        expUP.setText(args.currentBudget.budget)
        viewModel = ViewModelProvider(this)[BudgetViewModel::class.java]
        updateBudget.setOnClickListener {
            val dateUPtxt = dateUP.text.toString()
            val expUPtxt = expUP.text.toString()
            if(!TextUtils.isEmpty(dateUPtxt)&&!TextUtils.isEmpty(expUPtxt)){
                val updatedBudget = DailyBudgetEntity(args.currentBudget.id,dateUPtxt,expUPtxt)
                viewModel.updateTodayBudget(updatedBudget)
                findNavController().navigate(R.id.action_updateTodayBudgetFragment2_to_allBudgetListFragment)
            }
        }
        return view
    }
}