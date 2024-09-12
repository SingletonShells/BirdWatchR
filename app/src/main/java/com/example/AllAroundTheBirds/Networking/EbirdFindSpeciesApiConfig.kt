package com.example.AllAroundTheBirds.Networking

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EbirdFindSpeciesApiConfig {
    companion object {
        //The following code was taken from medium
        //Author : Krishna sundar
        //Link: https://medium.com/dsc-sastra-deemed-to-be-university/retrofit-with-viewmodel-in-kotlin-part-1-f9e705e77144
        fun getSpecies(): EbirdFindSpeciesService {

            // API response interceptor
            val loggingInterceptor = HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)

            // Client
            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()

            // Retrofit
            //GET ALL BIRD DATA https://api.ebird.org/v2/ref/taxonomy/ebird?key=""&fmt=''

            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.ebird.org/v2/ref/taxonomy/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            return retrofit.create(EbirdFindSpeciesService::class.java)
        }
        fun getHotspotApiService(): EbirdFindSpeciesService {

            // API response interceptor
            val loggingInterceptor = HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)

            // Client
            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()

            // Retrofit
            //GET Nearby hotspots https://api.ebird.org/v2/ref/hotspot/geo?lat={{lat}}&lng={{lng}} dist,fmt

            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.ebird.org/v2/ref/hotspot/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            return retrofit.create(EbirdFindSpeciesService::class.java)
        }
        fun getnearbyApiService(): EbirdFindSpeciesService {

            // API response interceptor
            val loggingInterceptor = HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)

            // Client
            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()

            // Retrofit
            //GET Recent nearby observations https://api.ebird.org/v2/data/obs/geo/recent?lat={{lat}}&lng={{lng}}

            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.ebird.org/v2/data/obs/geo/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            return retrofit.create(EbirdFindSpeciesService::class.java)
        }
        const val API_KEY = "6g1k2s5h6mt"
    }
}