package com.example.myapplication.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myapplication.model.ResponseModel
import com.example.myapplication.model.SavedResponseModel
import com.example.myapplication.model.SavingScreenModel

@Database(
    entities = [ResponseModel::class,SavingScreenModel::class, SavedResponseModel::class],
    version = 1
    )
abstract class DataBase : RoomDatabase(){

    abstract fun responseModelDAOCreater():ResponseModelDAO

}