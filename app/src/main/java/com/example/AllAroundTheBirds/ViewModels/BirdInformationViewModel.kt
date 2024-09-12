package com.example.AllAroundTheBirds.ViewModels

import android.content.Context
import android.net.ConnectivityManager
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.AllAroundTheBirds.DataClasses.MatchedSpecies
import com.example.AllAroundTheBirds.GetApiList.Ebird
import com.example.AllAroundTheBirds.GetApiList.Unsplash
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class BirdInformationViewModel: ViewModel() {

    //list of birds for adapter
    //The following code was taken from medium
    //Author : Jaykishan Sewak
    //Link: https://medium.com/@jecky999/livedata-vs-mutablelivedata-exploring-value-setting-methods-in-kotlin-9b52efe4e15a
    private val birdListLiveData: MutableLiveData<MutableList<MatchedSpecies>> = MutableLiveData()
    // Function to set the value of birdListLiveData
    private fun setBirdListData(birdList: MutableList<MatchedSpecies>) {
        birdListLiveData.value = birdList
    }
    fun observeBirdList(): LiveData<MutableList<MatchedSpecies>> = birdListLiveData

//check internet connection
//The following code was taken from medium
//Author : Shashank Mohabia
//Link: https://medium.com/@shashankmohabia/anhack-5-check-internet-connection-42746f0259a8
    private fun isNetworkConnected(context: Context): Boolean {
        val connectivityManager =context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
    fun updateList(context: Context) {
        //instantiate getapilists classes
        val ebird = Ebird(context)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                launch(Dispatchers.Main) {
                    if (isNetworkConnected(context)) {
                        ebird.fetchDataAndSaveToTextFile()
                    } else {
                        showToast(context, "Not connected to the internet cant update")
                    }
                }
            } catch (e: Exception) {
                messagesystem("updatebutton fail",e)
            }
        }

    }
    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
    fun messagesystem(place: String,message: Exception) {
        Log.d(place, message.toString())
    }

    //searchbutton click
    //The following code was taken from stackoverflow
    //Author : Gautam
    //Link: https://stackoverflow.com/questions/3072173/how-to-call-a-method-after-a-delay-in-android
    fun searchbtnclick(searchString:String, selectedOption:String,context: Context){
        //instantiate getapilists classes
        val ebird = Ebird(context)
        val unsplash = Unsplash(context)
        GlobalScope.launch(Dispatchers.IO) {
            try {
                launch(Dispatchers.Main) {
                    if (searchString.isNotBlank()) {
                        if (ebird.isEbirdDataFileExists()) {
                            val birdlist =
                                ebird.searchAndCollectMatches(searchString, selectedOption)
                            if (isNetworkConnected(context)) {

                                GlobalScope.launch(Dispatchers.IO) {
                                    try {
                                        launch(Dispatchers.Main) {
                                            //with null pictures
                                            val birdlistWithImages = runBlocking {
                                                unsplash.findspecies(birdlist)
                                            }

                                            setBirdListData(birdlistWithImages)

                                            Handler(Looper.getMainLooper()).postDelayed({
                                                setBirdListData(birdlistWithImages)
                                            },30000)
                                        }
                                    } catch (e: Exception) {
                                        messagesystem("isNetworkConnected true unsplash",e)
                                    }
                                }

                            } else {
                                GlobalScope.launch(Dispatchers.IO) {
                                    try {
                                        launch(Dispatchers.Main) {
                                            setBirdListData(birdlist)
                                        }
                                    } catch (e: Exception) {
                                        messagesystem("isNetworkConnected false",e)
                                    }
                                }
                            }

                        } else
                            showToast(context, "Please update bird file")
                    } else {

                        showToast(context, "Please enter a search term.")
                    }
                }
            } catch (e: Exception) {
                messagesystem("searchbutton fail", e)
            }
        }
    }
    //start of app
    fun startapp(context: Context){
        //instantiate getapilists classes
        val ebird = Ebird(context)
        if (isNetworkConnected(context)) {
            // Device is connected to the internet
            showToast(context, "Device is connected to the internet")
            //check  if ebird-txt exists
            if (!ebird.isEbirdDataFileExists()) {
                GlobalScope.launch(Dispatchers.IO) {
                    ebird.fetchDataAndSaveToTextFile()
                }
            }
        } else {
            showToast(
                context,
                "Device is not connected to the internet images wont be shown"
            )

        }
    }

}