package com.example.AllAroundTheBirds.Networking

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class unsplashApiConfig {
    companion object {
        //The following code was taken from medium
        //Author : Krishna sundar
        //Link: https://medium.com/dsc-sastra-deemed-to-be-university/retrofit-with-viewmodel-in-kotlin-part-1-f9e705e77144
        fun getPictureApiService(): UnsplashApiService {

            // API response interceptor
            val loggingInterceptor = HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)

            // Client
            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()

            // Retrofit
            //GET Bird Picture
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.unsplash.com/search/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            return retrofit.create(UnsplashApiService::class.java)
        }
        const val Access_Key = "w7OuPo26u3ty6ToMMCRMuCG1KqggQFdxQN3uPPWYvX8"
    }
}