package com.example.AllAroundTheBirds

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.AllAroundTheBirds.Adapters.FeedAdapter
import com.example.AllAroundTheBirds.MenuHelper.SideMenuHelper
import com.example.AllAroundTheBirds.ViewModels.FeedViewModel
import com.example.AllAroundTheBirds.databinding.ActivityFeedBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.navigation.NavigationView

class FeedActivity : AppCompatActivity() {
    lateinit var drawerLayout: DrawerLayout
    lateinit var sideMenuHelper: SideMenuHelper
    private lateinit var binding: ActivityFeedBinding
    private lateinit var viewModel: FeedViewModel
    // Request code for location permission
    private val LOCATION_PERMISSION_REQUEST_CODE = 123
    // Fused Location Provider Client
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(FeedViewModel::class.java)
        //side(menu)
        drawerLayout = binding.drawerLayout
        val navigationView: NavigationView = binding.navView
        val contentLayout: LinearLayout = binding.contentLayout
        sideMenuHelper = SideMenuHelper(this, drawerLayout)
        sideMenuHelper.setupMenu(navigationView, contentLayout)
        binding.imageView1.setOnClickListener {
            sideMenuHelper.openMenu()
        }
        //get location code

        //recycler view
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager


        // Initialize the FusedLocationProviderClient
        //The following code was taken from medium
        //Author : Droid By Me
        //Link: https://droidbyme.medium.com/get-current-location-using-fusedlocationproviderclient-in-android-cb7ebf5ab88e
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Check and request location permissions
        if (checkLocationPermission()) {
            // Permissions are granted, get the user's location
            getUserLocation()
        } else {
            // Request location permissions
            requestLocationPermission()
            if (checkLocationPermission()) {
                // Permissions are granted, get the user's location
                getUserLocation()

            } else {
                // Request location permissions
                requestLocationPermission()
                Toast.makeText(this, "location not available", Toast.LENGTH_SHORT).show()
            }
        }


        // when list changes recycler view auto updated
        viewModel.observeFeedList().observe(this) { updatedFeedList ->
            // Update the RecyclerView data when the bird list changes
            val adapter = FeedAdapter(updatedFeedList)
            binding.recyclerView.adapter = adapter


        }

        // view model instance
        //val viewModel: FeedViewModel = ViewModelProvider(this).get(FeedViewModel::class.java)

        binding.RefreshButton.setOnClickListener {
            if (checkLocationPermission()) {
                // Permissions are granted, get the user's location
                getUserLocation()

            } else {
                // Request location permissions
                requestLocationPermission()

            }
        }

    }
    private fun checkLocationPermission(): Boolean {
        val permissionState = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        return permissionState == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    private fun getUserLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location: Location? ->
            location?.let {
                viewModel.saveLocation(it.latitude, it.longitude)
                val latitude = it.latitude
                val longitude = it.longitude

            }
        }
        if(viewModel.getlat() != null && viewModel.getlong() != null){
            viewModel.updateFeedList(this, viewModel.getlat()!!,viewModel.getlong()!!)
        }else{
            Toast.makeText(this, "location not available", Toast.LENGTH_SHORT).show()
        }
    }
}