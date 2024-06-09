package com.sandeepprabhakula.budgetapp.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "daily_budget")
data class DailyBudgetEntity(
        @PrimaryKey(autoGenerate = true)
        val id:Int,
        @ColumnInfo(name = "today_date")
        val date:String,
        @ColumnInfo(name = "today_budget")
        val budget:String
):Parcelable