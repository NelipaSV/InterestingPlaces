package com.nsv.interestingplaces.network

import com.nsv.interestingplaces.model.Example
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FoursquareAPI {
    @GET("v2/venues/explore?client_id=N4LEHHBXUHHCHNASQJD4OS3CSBHGFL0YORQTN100LNHYV3TW&client_secret=1JROQEYKED3U31LA2D3RSFDI2SLNWHAN22JDWX25AB5YDYV5")
    fun getItems(
        @Query("ll") latLon: String,
        @Query("v") date: String
    ): Call<Example>
}