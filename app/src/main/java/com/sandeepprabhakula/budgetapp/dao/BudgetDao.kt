package com.sandeepprabhakula.budgetapp.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.sandeepprabhakula.budgetapp.entities.DailyBudgetEntity

@Dao
interface BudgetDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addTodayBudget(budget: DailyBudgetEntity)

    @Query("SELECT * FROM daily_budget ORDER BY id DESC")
    fun readAllBudget():LiveData<List<DailyBudgetEntity>>

    @Update
    fun updateTodayBudget(budget: DailyBudgetEntity)

    @Delete
    fun deleteAnyBudget(budget: DailyBudgetEntity)

    @Query("SELECT SUM( `today budget`) FROM DAILY_BUDGET")
    fun getTotalExpense():String
}