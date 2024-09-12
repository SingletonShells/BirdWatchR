package com.example.AllAroundTheBirds

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.LinearLayout
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.AllAroundTheBirds.Adapters.MatchedSpeciesAdapter
import com.example.AllAroundTheBirds.MenuHelper.SideMenuHelper
import com.example.AllAroundTheBirds.ViewModels.BirdInformationViewModel
import com.example.AllAroundTheBirds.databinding.ActivityBirdInformationBinding
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class BirdInformationActivity : AppCompatActivity() {
    lateinit var drawerLayout: DrawerLayout
    lateinit var sideMenuHelper: SideMenuHelper
    private lateinit var binding: ActivityBirdInformationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBirdInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)
//side(menu)
        drawerLayout = binding.drawerLayout
        val navigationView: NavigationView = binding.navView
        val contentLayout: LinearLayout = binding.contentLayout
        sideMenuHelper = SideMenuHelper(this, drawerLayout)
        sideMenuHelper.setupMenu(navigationView, contentLayout)
        binding.imageView1.setOnClickListener {
            sideMenuHelper.openMenu()
        }
        // view model instance
        //The following code was taken from tabnine
        //Author :
        //Link: https://www.tabnine.com/code/java/methods/androidx.lifecycle.ViewModelProvider/get

        val viewModel: BirdInformationViewModel = ViewModelProvider(this).get(BirdInformationViewModel::class.java)

        viewModel.startapp(this)
        //adapter code
        //val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val layoutManager = LinearLayoutManager(this) // You can also pass context here
        binding.recyclerView.layoutManager = layoutManager


        //empty list
        val adapter = MatchedSpeciesAdapter(mutableListOf())
        //val adapter = MatchedSpeciesAdapter(matchedSpeciesList)
        binding.recyclerView.adapter = adapter

        // when list changes recycler view auto updated
        //The following code was taken from stackoverflow
        //Author : Hernâni Pereira
        //Link: https://stackoverflow.com/questions/61909608/android-viewmodel-observe-mutablelivedataarrayliststring

        viewModel.observeBirdList().observe(this) { updatedBirdList ->
            // Update the RecyclerView data when the bird list changes
            adapter.updateData(updatedBirdList)
            // Schedule a delayed update every 20 seconds (20000 milliseconds)
            val handler = Handler(Looper.getMainLooper())
            val updateInterval = 30000L // 20 seconds

            val periodicUpdate: Runnable = object : Runnable {
                override fun run() {
                    adapter.updateData(updatedBirdList)
                    handler.postDelayed(this, updateInterval)
                }
            }

            handler.postDelayed(periodicUpdate, updateInterval)
        }


        binding.searchButton.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    launch(Dispatchers.Main) {
                        val selectedRadioButtonId =binding.radioGroup.checkedRadioButtonId
                        val selectedRadioButton = findViewById<RadioButton>(selectedRadioButtonId)
                        if (selectedRadioButtonId != -1) {

                            viewModel.searchbtnclick(binding.searchEditText.text.toString(),
                                selectedRadioButton.text.toString(),
                                applicationContext)

                        } else {
                            viewModel.showToast(applicationContext,"Please select an option.")
                        }
                    }
                } catch (e: Exception) {
                    viewModel.messagesystem("searchbutton fail",e)
                }
            }
        }

        binding.update.setOnClickListener{
            viewModel.updateList(this)
        }
    }


}