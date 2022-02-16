package com.sandeepprabhakula.budgetapp.repository

import androidx.lifecycle.LiveData
import com.sandeepprabhakula.budgetapp.entities.DailyBudgetEntity
import com.sandeepprabhakula.budgetapp.dao.BudgetDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class BudgetRepository(private val budgetDao: BudgetDao) {
    val readAllBudget:LiveData<List<DailyBudgetEntity>> = budgetDao.readAllBudget()

    fun addTodayBudget(budget: DailyBudgetEntity){
        GlobalScope.launch (Dispatchers.IO){
            budgetDao.addTodayBudget(budget)
        }
    }
    fun updateTodayBudget(budget: DailyBudgetEntity){
        GlobalScope.launch(Dispatchers.IO){
            budgetDao.updateTodayBudget(budget)
        }
    }
    fun deleteAnyBudget(budget: DailyBudgetEntity){
        GlobalScope.launch(Dispatchers.IO){
            budgetDao.deleteAnyBudget(budget)
        }
    }
}