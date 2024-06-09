package com.sandeepprabhakula.budgetapp.roomDBSingleton

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sandeepprabhakula.budgetapp.entities.DailyBudgetEntity
import com.sandeepprabhakula.budgetapp.dao.BudgetDao

@Database(entities = [DailyBudgetEntity::class], version = 1, exportSchema = false)
abstract class BudgetDatabase :RoomDatabase() {
    abstract fun budgetDao(): BudgetDao
    companion object{
        @Volatile
        private var INSTANCE: BudgetDatabase? = null
        fun getDatabase(context:Context): BudgetDatabase {
            val tempInstance = INSTANCE
            if(tempInstance!=null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(context.applicationContext,
                BudgetDatabase::class.java,
                "daily_budget")
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}