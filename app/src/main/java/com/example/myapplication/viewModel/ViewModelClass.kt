package com.example.myapplication.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.R
import com.example.myapplication.model.ResponseModel
import com.example.myapplication.room.DataBase

class ViewModelClass : ViewModel(){

    val user = MutableLiveData("")
    val password = MutableLiveData("")

    val listGet = ArrayList<ResponseModel>()

    var dataBase: DataBase? = null

    val selectedValue =  MutableLiveData<ResponseModel?>(null)

    fun fieldTextChanged(charSequence: CharSequence, idField: Int){
        when(idField) {
            R.id.user -> user.value = charSequence.toString()
            R.id.password -> password.value = charSequence.toString()
        }
    }
}