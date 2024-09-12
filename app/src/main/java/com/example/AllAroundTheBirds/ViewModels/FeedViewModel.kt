package com.example.AllAroundTheBirds.ViewModels

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.AllAroundTheBirds.DataClasses.Feed
import com.example.AllAroundTheBirds.GetApiList.Ebird
import com.example.AllAroundTheBirds.GetApiList.Unsplash
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class FeedViewModel: ViewModel() {
    //list of birds for adapter
    private val feedListLiveData: MutableLiveData<MutableList<Feed>> = MutableLiveData()
    // Function to set the value of birdListLiveData
    private fun setFeedListData(feedList: MutableList<Feed>) {
        feedListLiveData.value = feedList
    }
    fun observeFeedList(): LiveData<MutableList<Feed>> = feedListLiveData
    private var latitude: Double? = null
    private var longitude: Double? = null

    fun saveLocation(latitude: Double, longitude: Double) {
        this.latitude = latitude
        this.longitude = longitude
    }

    fun getlat(): Double? {
        return latitude
    }
    fun getlong(): Double? {
        return longitude
    }

    private fun isNetworkConnected(context: Context): Boolean {
        val connectivityManager =context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }


    fun updateFeedList(context: Context,latitude: Double, longitude: Double) {
        //instantiate getapilists classes
        val ebird = Ebird(context)
        val unsplash = Unsplash(context)
        //The following code was taken from geeksforgeeks
        //Author : lavishgarg26
        //Link: https://www.geeksforgeeks.org/runblocking-in-kotlin-coroutines-with-example/
        GlobalScope.launch(Dispatchers.IO) {
            try {
                launch(Dispatchers.Main) {
                    if (isNetworkConnected(context)) {
                        val Feedlistnoimage = runBlocking {
                            ebird.getNearbBirdObservations(latitude.toString(),longitude.toString(), dist = "30")
                        }
                        val FeedlistWithImages = runBlocking {
                            unsplash.findFeed(Feedlistnoimage)
                        }
                        setFeedListData(FeedlistWithImages)
                    } else {
                        showToast(context, "Not connected to the internet cant Display Feed")
                    }
                }
            } catch (e: Exception) {
                messagesystem("refreshbutton fail",e)
            }
        }
    }

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
    fun messagesystem(place: String,message: Exception) {
        Log.d(place, message.toString())
    }
}