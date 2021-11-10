package com.example.myapplication.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.AdapterResponse
import com.example.myapplication.App
import com.example.myapplication.DialogPersonalized
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentHomeBinding
import com.example.myapplication.model.ResponseModel
import com.example.myapplication.proxi.RetrofitController
import com.example.myapplication.viewModel.ViewModelClass
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.koin.android.ext.android.inject

class HomeFragment : Fragment() {

    private val viewModel: ViewModelClass by activityViewModels()
    private val retrofitController: RetrofitController by inject()
    private val dialog: DialogPersonalized by inject()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding: FragmentHomeBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog.context = context
        recycler_response.layoutManager = LinearLayoutManager(requireContext())
        lifecycleScope.launch {
            val responseModelRequest = viewModel.dataBase!!.responseModelDAOCreater().getAll()
            if(responseModelRequest.isEmpty()){
                refreshRequest()
            }else{
                recycler_response.adapter = AdapterResponse(viewModel,responseModelRequest as ArrayList<ResponseModel>)
            }
        }

    }

    private fun refreshRequest(){
        charging.setAnimation(R.raw.listen)
        charging.playAnimation()
        charging.visibility = View.VISIBLE
        retrofitController.executeAPI ("",{
            if (it.code().toString() == "200") {
                val jsonData = JSONObject(it.body().toString()).getJSONArray("employees")
                for (i in 0 until jsonData.length()) {
                    val jsonObj = jsonData[i] as JSONObject
                    viewModel.listGet.add(
                        ResponseModel(
                            jsonObj.getString("id"),
                            jsonObj.getString("firstName"),
                            jsonObj.getString("lastName"),
                            jsonObj.getString("image"),
                            jsonObj.getString("description"),
                            jsonObj.getString("rating")
                        )
                    )
                }
                lifecycleScope.launch {
                    viewModel.dataBase!!.responseModelDAOCreater().insertList(viewModel.listGet)
                }
                recycler_response.adapter = AdapterResponse(viewModel,viewModel.listGet)
            } else {
                dialog.contenido = "Respuesta del servidor invalida."
                dialog.showDialog()
            }
            charging.visibility=View.GONE
            charging.clearAnimation()
        },{
            dialog.contenido = "La información no pudo ser recibida satisfactoriamente, revise su conexion a internet"
            dialog.showDialog()
            charging.visibility=View.GONE
            charging.clearAnimation()
        })
    }

}