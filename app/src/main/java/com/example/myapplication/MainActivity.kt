package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import com.example.myapplication.model.SavedResponseModel
import com.example.myapplication.model.SavingScreenModel
import com.example.myapplication.room.DataBase
import com.example.myapplication.utils.animation.Animations
import com.example.myapplication.viewModel.ViewModelClass
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private var navController : NavController? = null
    private var fragmentId: Int? = null
    private var fragmentView: View? = null
    private val viewModel: ViewModelClass by viewModels()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        navController =
            (supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment).findNavController()
        navController!!.addOnDestinationChangedListener {_, destination, _ ->
            fragmentId = destination.id
        }
        fragmentView = fragment
        viewModel.dataBase = Room.databaseBuilder(this, DataBase::class.java,"model").build()
        lifecycleScope.launch {
            val screenList = viewModel.dataBase!!.responseModelDAOCreater().getScreen()
            if(screenList.isNotEmpty()){
                when(screenList[0].screen){
                    1 -> Navigation.findNavController(fragment).navigate(R.id.home_fragment)
                    2 ->{
                        Navigation.findNavController(fragment).navigate(R.id.details_fragment)
                        viewModel.selectedValue.value = (viewModel.dataBase!!.responseModelDAOCreater().getAll().filter {
                            it.id == viewModel.dataBase!!.responseModelDAOCreater().getResponseSaved()[0]
                                .responseModelId
                        })[0]
                    }
                }
            }


        }
    }

    override fun onPause() {
        super.onPause()
        lifecycleScope.launch {
            var numbScreen = 0
            when(fragmentId){
                R.id.details_fragment-> {
                    viewModel.dataBase!!.responseModelDAOCreater().insertResponseSaved(
                        SavedResponseModel(responseModelId = viewModel.selectedValue.value!!.id)
                    )
                    numbScreen =  2
                }
                R.id.home_fragment-> numbScreen = 1
                else -> numbScreen = 0
            }
            viewModel.dataBase!!.responseModelDAOCreater().insertScreen(SavingScreenModel(screen = numbScreen))
        }
    }

    override fun onBackPressed(){
        when(fragmentId){
            R.id.details_fragment-> Navigation.findNavController(fragment).navigate(R.id.home_fragment,null, Animations.options_slide_out)
            R.id.home_fragment-> Navigation.findNavController(fragment).navigate(R.id.login_fragment,null, Animations.options_slide_out)
         }
    }


}