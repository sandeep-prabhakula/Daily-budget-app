package com.sandeepprabhakula.budgetapp.fragments.read

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sandeepprabhakula.budgetapp.R
import com.sandeepprabhakula.budgetapp.adapter.ReadDailyBudgetAdapter
import com.sandeepprabhakula.budgetapp.adapter.UpdateBudget
import com.sandeepprabhakula.budgetapp.entities.DailyBudgetEntity
import com.sandeepprabhakula.budgetapp.viewModel.BudgetViewModel

class AllBudgetListFragment : Fragment(), UpdateBudget {
    private lateinit var viewModel: BudgetViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_all_budget_list, container, false)
        val fab: FloatingActionButton = view.findViewById(R.id.floatingActionButton)
        fab.setOnClickListener {
            findNavController().navigate(R.id.action_allBudgetListFragment_to_addTodayBudgetFragment)
        }

        val budgetList: RecyclerView = view.findViewById(R.id.recyclerView)
        val adapter = ReadDailyBudgetAdapter(this)
        budgetList.adapter = adapter
        budgetList.layoutManager = LinearLayoutManager(requireContext())

        viewModel = ViewModelProvider(this)[BudgetViewModel::class.java]
        viewModel.readAllBudget.observe(viewLifecycleOwner) {
            adapter.setData(it)
        }
        val itemTouchHelperCallback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewModel.deleteAnyBudget(adapter.getNoteAt(viewHolder.adapterPosition))
                Toast.makeText(requireContext(),"Budget deleted",Toast.LENGTH_SHORT).show()
            }

        }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(budgetList)
        return view
    }

    override fun onClickCurrentBudget(budget: DailyBudgetEntity) {
        val action =
            AllBudgetListFragmentDirections.actionAllBudgetListFragmentToUpdateTodayBudgetFragment2(
                budget
            )
        findNavController().navigate(action)
    }
}