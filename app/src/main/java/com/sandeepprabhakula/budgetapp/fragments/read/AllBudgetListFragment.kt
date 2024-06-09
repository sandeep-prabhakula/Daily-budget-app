package com.sandeepprabhakula.budgetapp.fragments.read

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.sandeepprabhakula.budgetapp.R
import com.sandeepprabhakula.budgetapp.adapter.ReadDailyBudgetAdapter
import com.sandeepprabhakula.budgetapp.adapter.UpdateBudget
import com.sandeepprabhakula.budgetapp.entities.DailyBudgetEntity
import com.sandeepprabhakula.budgetapp.viewModel.BudgetViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
        val totalCost: TextView = view.findViewById(R.id.totalCost)
        budgetList.adapter = adapter
        budgetList.layoutManager = LinearLayoutManager(requireContext())
        budgetList.addItemDecoration(
            DividerItemDecoration(
                budgetList.context,
                DividerItemDecoration.VERTICAL
            )
        )
        viewModel = ViewModelProvider(this)[BudgetViewModel::class.java]
        viewModel.readAllBudget.observe(viewLifecycleOwner) {
            adapter.setData(it)
        }
        CoroutineScope(Dispatchers.IO).launch {
            val cost = viewModel.getTotalExpense()
            withContext(Dispatchers.Main) {

                totalCost.text = "Rs. $cost"
            }
        }
        val itemTouchHelperCallback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if(direction==ItemTouchHelper.LEFT){

                    val position = viewHolder.adapterPosition
                    val deletedNote = adapter.getNoteAt(position)
                    val deleteJob = lifecycleScope.launch(Dispatchers.IO) {
                        viewModel.deleteAnyBudget(adapter.getNoteAt(viewHolder.adapterPosition))
                    }
                    lifecycleScope.launch(Dispatchers.IO) {
                        deleteJob.join()
                        val cost = viewModel.getTotalExpense()
                        withContext(Dispatchers.Main) {
                            totalCost.text = "Rs.$cost"
                        }
                    }
                    Snackbar.make(
                        budgetList,
                        "Deleted Budget of date ${deletedNote.date}",
                        Snackbar.LENGTH_LONG
                    )
                        .setAction("UNDO") {
                            val undoJob = lifecycleScope.launch(Dispatchers.IO) {
                                viewModel.addTodayBudget(deletedNote)
                            }

                            lifecycleScope.launch(Dispatchers.IO) {
                                undoJob.join()
                                val cost = viewModel.getTotalExpense()
                                withContext(Dispatchers.Main) {
                                    totalCost.text = "Rs.$cost"
                                }
                            }
                        }
                        .show()
                }else if(direction == ItemTouchHelper.RIGHT){
                    val position = viewHolder.adapterPosition
                    val budget = adapter.getNoteAt(position)
                    onClickCurrentBudget(budget)
                }
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    val itemView = viewHolder.itemView
                    val p = Paint()
                    val textSize = 40f
                    val textColor = Color.WHITE
                    val text: String
                    val backgroundColor: Int

                    if (dX > 0) {
                        // Swipe right
                        text = "Edit"
                        backgroundColor = Color.BLUE
                    } else {
                        // Swipe left
                        text = "Deleting..."
                        backgroundColor = Color.RED
                    }

                    // Set background color
                    p.color = backgroundColor
                    if (dX > 0) {
                        // Draw background on the right
                        c.drawRect(
                            itemView.left.toFloat(),
                            itemView.top.toFloat(),
                            dX,
                            itemView.bottom.toFloat(),
                            p
                        )
                    } else {
                        // Draw background on the left
                        c.drawRect(
                            itemView.right.toFloat() + dX,
                            itemView.top.toFloat(),
                            itemView.right.toFloat(),
                            itemView.bottom.toFloat(),
                            p
                        )
                    }

                    // Set text properties
                    p.color = textColor
                    p.textSize = textSize
                    p.textAlign = Paint.Align.LEFT
                    // Calculate text position
                    val textMargin = (itemView.height / 2).toFloat() - (textSize / 2)
                    val textY = itemView.top.toFloat() + textMargin + textSize
                    val textX: Float = if (dX > 0) {
                        itemView.left.toFloat() + textMargin
                    } else {
                        itemView.right.toFloat() + dX + textMargin
                    }

                    // Draw text
                    c.drawText(text, textX, textY, p)
                }

                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
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