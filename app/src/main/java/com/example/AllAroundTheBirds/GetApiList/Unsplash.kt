package com.example.AllAroundTheBirds.GetApiList

import android.content.Context
import android.util.Log
import com.example.AllAroundTheBirds.DataClasses.Feed
import com.example.AllAroundTheBirds.DataClasses.MatchedSpecies
import com.example.AllAroundTheBirds.Networking.unsplashApiConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope


class Unsplash(private val context: Context) {
    val unsplashService = unsplashApiConfig.getPictureApiService()

    //The following code was taken from geeksforgeeks
    //Author : lavishgarg26
    //Link: https://www.geeksforgeeks.org/suspend-function-in-kotlin-coroutines/
    suspend fun findspecies(matchedSpeciesList: MutableList<MatchedSpecies>): MutableList<MatchedSpecies> {
        return coroutineScope {
            val deferredSpeciesList = matchedSpeciesList.map { species ->
                async(Dispatchers.IO) {
                    val query = species.comName
                    if (query != null) {

                        val call = unsplashService.getSightedBird(query = query,page="1",per_page="1")
                        val response = try {
                            call.execute()
                        } catch (e: Exception) {
                            Log.d("Species", "ResponseApi error: $e")
                            null

                        }

                        if (response != null && response.isSuccessful) {
                            Log.d("Species", "ResponseApi: $response")
                            val images = response.body()
                            if (images!=null && images.results?.isNotEmpty() == true) {
                                val regularUrl = images.results?.get(0)?.urls?.regular
                                Log.d("Species", "Reg: $regularUrl")
                                species.picture = regularUrl
                            }
                        } else {
                            // Handle API error here if needed
                        }
                        return@async species
                    } else {
                        //
                    }
                }
            }

            deferredSpeciesList.awaitAll()
            logSpeciesFields(matchedSpeciesList)
            return@coroutineScope matchedSpeciesList
        }
    }
    fun logSpeciesFields(matchedSpeciesList: List<MatchedSpecies>) {
        for (species in matchedSpeciesList) {
            Log.d("SpeciesInfo", "full: $species")
            Log.d("SpeciesInfo", "Common Name: ${species.comName}")
            Log.d("SpeciesInfo", "Scientific Name: ${species.sciName}")
            Log.d("SpeciesInfo", "Picture URL: ${species.picture}")
            // Add more fields as needed
        }
    }
    suspend fun findFeed(FeedList: MutableList<Feed>): MutableList<Feed> {
        return coroutineScope {
            val deferredSpeciesList = FeedList.map { feeds ->
                async(Dispatchers.IO) {
                    val query = feeds.comName
                    if (query != null) {

                        val call = unsplashService.getSightedBird(query = query,page="1",per_page="1")
                        val response = try {
                            call.execute()
                        } catch (e: Exception) {
                            Log.d("Species", "ResponseApi error: $e")
                            null

                        }

                        if (response != null && response.isSuccessful) {
                            Log.d("Species", "ResponseApi: $response")
                            val images = response.body()
                            if (images!=null && images.results?.isNotEmpty() == true) {
                                val regularUrl = images.results?.get(0)?.urls?.regular
                                Log.d("Species", "Reg: $regularUrl")
                                feeds.picture = regularUrl
                            }
                        } else {
                            // Handle API error here if needed
                        }
                        return@async feeds
                    } else {
                        //
                    }
                }
            }

            deferredSpeciesList.awaitAll()
            return@coroutineScope FeedList
        }
    }
}