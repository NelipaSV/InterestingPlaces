package com.nsv.interestingplaces.viewModel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

class MainVmFactory(private val latLon: String, private val date: String) : ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MainVM::class.java)) {
            MainVM(latLon, date) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}