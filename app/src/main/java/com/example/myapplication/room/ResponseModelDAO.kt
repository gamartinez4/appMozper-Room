package com.example.myapplication.room
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myapplication.model.ResponseModel
import com.example.myapplication.model.SavedResponseModel
import com.example.myapplication.model.SavingScreenModel

@Dao
interface ResponseModelDAO {
    @Query("SELECT * FROM ResponseModel")
    suspend fun getAll():List<ResponseModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(responseModelList: ArrayList<ResponseModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScreen(screen:SavingScreenModel)

    @Query("SELECT * FROM SavingScreenModel where id = 1")
    suspend fun getScreen():List<SavingScreenModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResponseSaved(savedResponseModel:SavedResponseModel)

    @Query("SELECT * FROM SavedResponseModel")
    suspend fun getResponseSaved():List<SavedResponseModel>


}