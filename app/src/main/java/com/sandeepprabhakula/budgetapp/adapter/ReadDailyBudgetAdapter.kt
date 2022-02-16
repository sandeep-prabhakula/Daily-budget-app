package com.sandeepprabhakula.budgetapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.sandeepprabhakula.budgetapp.entities.DailyBudgetEntity
import com.sandeepprabhakula.budgetapp.R

class ReadDailyBudgetAdapter (private val listener:UpdateBudget): RecyclerView.Adapter<ReadDailyBudgetAdapter.MyViewHolder>() {

    private var budgetList = emptyList<DailyBudgetEntity>()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val date:TextView = itemView.findViewById(R.id.dateFromDB)
        val exp:TextView = itemView.findViewById(R.id.expenditureFromDB)
        val currentItem:CardView = itemView.findViewById(R.id.currentItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.daily_budget_row, parent, false)
        )
        view.currentItem.setOnClickListener {
            listener.onClickCurrentBudget(budgetList[view.adapterPosition])
        }
        return view
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = budgetList[position]
        holder.date.text = currentItem.date
        holder.exp.text = currentItem.budget
    }
    fun setData(budgetList:List<DailyBudgetEntity>){
        this.budgetList = budgetList
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
        return budgetList.size
    }
    fun getNoteAt(position: Int):DailyBudgetEntity{
        return budgetList[position]
    }
}
interface UpdateBudget{
    fun onClickCurrentBudget(budget: DailyBudgetEntity)
}