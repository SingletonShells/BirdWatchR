package com.example.AllAroundTheBirds.Networking

import com.example.AllAroundTheBirds.DataClasses.UnsplashImageResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface UnsplashApiService {
    //wanted bird picture from unsplash
    //The following code was taken from medium
    //Author : Krishna sundar
    //Link: https://medium.com/dsc-sastra-deemed-to-be-university/retrofit-with-viewmodel-in-kotlin-part-1-f9e705e77144
    @GET("photos")
    fun getSightedBird(
        @Query("client_id") key: String = unsplashApiConfig.Access_Key,
        @Query("page") page: String ="1",
        @Query("query") query: String,
        @Query("per_page") per_page: String ="1"
    ): Call<UnsplashImageResponse>
}