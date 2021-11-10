package com.example.myapplication.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SavingScreenModel (
    @PrimaryKey
    val id:Int = 1,
    val screen : Int
)