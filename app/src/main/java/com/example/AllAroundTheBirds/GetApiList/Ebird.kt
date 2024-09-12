package com.example.AllAroundTheBirds.GetApiList

import android.content.Context
import android.util.Log
import com.example.AllAroundTheBirds.DataClasses.Feed
import com.example.AllAroundTheBirds.DataClasses.FindSpeciesResponseItem
import com.example.AllAroundTheBirds.DataClasses.Hotspots
import com.example.AllAroundTheBirds.DataClasses.MatchedSpecies
import com.example.AllAroundTheBirds.Networking.EbirdFindSpeciesApiConfig
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.FileReader
import java.io.IOException
import java.util.Locale

class Ebird(private val context: Context) {
    //for find species
    private val ebirdService = EbirdFindSpeciesApiConfig.getSpecies()
    private val ebirdService2 = EbirdFindSpeciesApiConfig.getnearbyApiService()
    private val ebirdService3 = EbirdFindSpeciesApiConfig.getHotspotApiService()
    // Function to fetch data from the eBird API and SAVE TO TXT FILE

    //The following code was taken from stackoverflow
    //Author :  Andrew
    //Link: https://stackoverflow.com/questions/53492348/handle-retrofit-callback-using-kotlin
    fun fetchDataAndSaveToTextFile() {

        ebirdService.getSpecies()
        .enqueue(object : Callback<List<FindSpeciesResponseItem>> {
            override fun onResponse(
                call: Call<List<FindSpeciesResponseItem>>,
                response: Response<List<FindSpeciesResponseItem>>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()
                    data?.let {
                        saveDataToFile(it)
                    }
                } else {
                // Handle API error
                }
            }

            override fun onFailure(
                call: Call<List<FindSpeciesResponseItem>>,
                t: Throwable
            ) {
            // Handle network failure
            }
        })
    }
    //save the data to a txt file for offline
    //The following code was taken from java2s
    //Author : Unknown
    //Link: http://www.java2s.com/example/java-api/java/io/fileoutputstream/write-1-10.html
    private fun saveDataToFile(data: List<FindSpeciesResponseItem>) {
        try {
            val filename = "ebird_data.txt"
            val filePath = File(context.externalCacheDir, filename)

            // Delete the existing file if it exists
            if (filePath.exists()) {
                filePath.delete()
            }
            // Create a FileOutputStream
            val fileOutputStream = FileOutputStream(filePath)

            // Write the JSON string to the file
            // Convert the data to a JSON string before writing it
            val jsonString = Gson().toJson(data)
            fileOutputStream.write(jsonString.toByteArray())

            // Close the FileOutputStream to release resources
            fileOutputStream.close()

            // You can now access the saved JSON file using filePath
            // filePath.absolutePath will give you the full path to the saved file
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }
    //check if the file exists
    fun isEbirdDataFileExists(): Boolean {
        val filename = "ebird_data.txt"
        val filePath = File(context.externalCacheDir, filename)
        return filePath.exists()
    }
    // Function to read and convert the file to a list of objects
    //The following code was taken from stackoverflow
    //Author : Matthew Quiros
    //Link: https://stackoverflow.com/questions/12384064/gson-convert-from-json-to-a-typed-arraylistt
    fun readDataFile(): List<FindSpeciesResponseItem>? {
        val filename = "ebird_data.txt"
        val filePath = File(context.externalCacheDir, filename)

        try {

            val fileReader = FileReader(filePath)
            val bufferedReader = BufferedReader(fileReader)
            val stringBuilder = StringBuilder()
            var line: String?

            while (bufferedReader.readLine().also { line = it } != null) {
                stringBuilder.append(line)
            }

            bufferedReader.close()

            val jsonString = stringBuilder.toString()

            val listType = object : TypeToken<List<FindSpeciesResponseItem>>() {}.type
            return Gson().fromJson(jsonString, listType)

        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }
    //Search Textfile for the users common name input RETURN LIST WITH NULL IMAGE
    //The following code was taken from stackoverflow
    //Author : Aseem Sharma
    //Link: https://stackoverflow.com/questions/51238456/how-to-check-whether-a-string-contains-a-substring-in-kotlin
    fun searchAndCollectMatches(userInput: String,selectedOption:String): MutableList<MatchedSpecies> {
        val allSpecies = readDataFile() // Read data from the file

        val matchingItems = mutableListOf<MatchedSpecies>()
        val userInputLowerCase = userInput.lowercase(Locale.getDefault())

        // Iterate through all species and collect matching items
        allSpecies?.forEach { species ->
            val nameToSearch: String? = when (selectedOption) {
                "Common Name" -> species.comName
                "Scientific Name" -> species.sciName
                else -> null // Handle invalid selectedOption
            }
            nameToSearch?.let { name ->
                val nameLowerCase = name.lowercase(Locale.getDefault())
                if (nameLowerCase.contains(userInputLowerCase)) {
                    // Match found, add to the list
                    matchingItems.add(
                        MatchedSpecies(
                            comName = species.comName,
                            sciName = species.sciName,
                            category = species.category,
                            picture = null
                        )
                    )
                }
            }
            // Stop when 10 matching items are found
            if (matchingItems.size >= 10) {
                Log.d("searchAndCollectMatches", "matchingItems Size 10?: ${matchingItems.size}")
                return matchingItems
            }
        }
        Log.d("searchAndCollectMatches", "matchingItems Size less 10?: ${matchingItems.size}")
        return matchingItems
    }

    //GET Recent nearby observations
    //The following code was taken from geeksforgeeks
    //Author : lavishgarg26
    //Link: https://www.geeksforgeeks.org/suspend-function-in-kotlin-coroutines/
    suspend fun getNearbBirdObservations(
        lat: String,
        lng: String,
        dist: String
    ): MutableList<Feed> = withContext(Dispatchers.IO) {
        val feedList = mutableListOf<Feed>()

        try {
            val response = ebirdService2.getNearbyBird(lat = lat, lng = lng, dist = dist).execute()

            if (response.isSuccessful) {
                val nearbyObservations = response.body()
                if (nearbyObservations != null) {
                    feedList.addAll(nearbyObservations.map {
                        Feed(it.comName, it.locName, it.obsDt, null)
                    })
                }
            } else {
                //do later
                TODO("WHAT TO PUT HERE")
            }
        } catch (e: Exception) {
            Log.d("getNearbyBirdObservations", "API call error: $e")
        }

        feedList
    }
    //GET Nearby hotspots
    suspend fun getNearbyHotspots(
        lat: String,
        lng: String,
        dist: String
    ): MutableList<Hotspots> = withContext(Dispatchers.IO) {
        val hotspotList = mutableListOf<Hotspots>()

        try {
            val response = ebirdService3.getBirdHotspot(lat = lat, lng = lng, dist = dist).execute()

            if (response.isSuccessful) {
                val nearbyHotspots = response.body()
                if (nearbyHotspots != null) {
                    hotspotList.addAll(nearbyHotspots.map {
                        Hotspots(it.locName, it.lat.toString(), it.lng.toString(), it.numSpeciesAllTime.toString())
                    })
                }
            } else {
                //do later
                TODO("WHAT TO PUT HERE")
            }
        } catch (e: Exception) {
            Log.d("getNearbyHotspots", "API call error: $e")
        }

        hotspotList
    }
}