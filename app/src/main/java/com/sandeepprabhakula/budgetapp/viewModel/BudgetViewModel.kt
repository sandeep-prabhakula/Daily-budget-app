package com.sandeepprabhakula.budgetapp.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.sandeepprabhakula.budgetapp.roomDBSingleton.BudgetDatabase
import com.sandeepprabhakula.budgetapp.repository.BudgetRepository
import com.sandeepprabhakula.budgetapp.entities.DailyBudgetEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BudgetViewModel(application: Application) : AndroidViewModel(application) {
    val readAllBudget: LiveData<List<DailyBudgetEntity>>
    private val repository: BudgetRepository

    init {
        val budgetDao = BudgetDatabase.getDatabase(application).budgetDao()
        repository = BudgetRepository(budgetDao)
        readAllBudget = repository.readAllBudget
    }

    fun addTodayBudget(budget: DailyBudgetEntity) {
        viewModelScope.launch (Dispatchers.IO){
            repository.addTodayBudget(budget)
        }
    }

    fun updateTodayBudget(budget: DailyBudgetEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTodayBudget(budget)
        }
    }

    fun deleteAnyBudget(budget: DailyBudgetEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAnyBudget(budget)
        }
    }
}