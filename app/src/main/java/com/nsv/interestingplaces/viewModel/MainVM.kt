package com.nsv.interestingplaces.viewModel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.nsv.interestingplaces.model.Example
import com.nsv.interestingplaces.model.Item
import com.nsv.interestingplaces.network.ClientAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainVM(latLon: String, date: String) : ViewModel() {

    private val places : MutableLiveData<List<Item>> by lazy {
        MutableLiveData<List<Item>>().also {
            loadPlaces(latLon, date)
        }
    }

    private fun loadPlaces(latLon: String, date: String){
        val foursquareAPI = ClientAPI().create()
        val call = foursquareAPI.getItems(latLon, date)

        call.enqueue(object : Callback<Example> {
            override fun onFailure(call: Call<Example>, t: Throwable) {
                Log.i("ERROR RETROFIT", "call failed")
            }

            override fun onResponse(call: Call<Example>, response: Response<Example>) {
                Log.i("RETROFIT", ""+response.body())
                places.value = response.body()!!.response.groups[0].items
            }
        })
    }

    fun getPlacesMap() : MutableLiveData<List<Item>>{
        return places
    }

}