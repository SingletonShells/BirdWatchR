package com.example.AllAroundTheBirds.Networking

import com.example.AllAroundTheBirds.DataClasses.EbirdNearbyHotspotItem
import com.example.AllAroundTheBirds.DataClasses.EbirdNearbyObservationItem
import com.example.AllAroundTheBirds.DataClasses.FindSpeciesResponseItem
import retrofit2.Call
import retrofit2.http.*

interface EbirdFindSpeciesService {
    //The following code was taken from medium
    //Author : Krishna sundar
    //Link: https://medium.com/dsc-sastra-deemed-to-be-university/retrofit-with-viewmodel-in-kotlin-part-1-f9e705e77144
    // Get all bird data
    @GET("ebird")
    fun getSpecies(
        @Query("key") key: String = EbirdFindSpeciesApiConfig.API_KEY,
        @Query("fmt") fmt: String ="json"
    ): Call<List<FindSpeciesResponseItem>>

    //recent sightings
    @GET("recent")
    fun getNearbyBird(
        @Query("key") key: String = EbirdFindSpeciesApiConfig.API_KEY,
        @Query("lat") lat: String,
        @Query("lng") lng: String,
        @Query("dist") dist: String,
        @Query("maxResults") maxResults: String = "15"
    ): Call<List<EbirdNearbyObservationItem>>

    // Nearby hotspots
    @GET("geo")
    fun getBirdHotspot(
        @Query("key") key: String = EbirdFindSpeciesApiConfig.API_KEY,
        @Query("lat") lat: String,
        @Query("lng") lng: String,
        @Query("fmt") fmt: String = "json",
        @Query("dist") dist: String,
        @Query("maxResults") maxResults: String = "10"
    ): Call<List<EbirdNearbyHotspotItem>>
}