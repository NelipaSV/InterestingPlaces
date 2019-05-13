package com.nsv.interestingplaces.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ClientAPI {

    fun create(): FoursquareAPI {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.foursquare.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create<FoursquareAPI>(FoursquareAPI::class.java)
    }
}