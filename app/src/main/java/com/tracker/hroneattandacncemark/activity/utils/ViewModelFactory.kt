package com.tracker.hroneattandacncemark.activity.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tracker.hroneattandacncemark.activity.MainViewModel
import com.tracker.hroneattandacncemark.network.Api
import com.tracker.hroneattandacncemark.utils.AppPreferences

class ViewModelFactory(private val appPreferences: AppPreferences,private val networkClient: Api) :ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MainViewModel::class.java))
            return MainViewModel(appPreferences,networkClient) as T
        else throw IllegalArgumentException("Unknown ViewModel class")
    }
}