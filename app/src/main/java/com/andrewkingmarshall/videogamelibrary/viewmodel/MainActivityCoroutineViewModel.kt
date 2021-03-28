package com.andrewkingmarshall.videogamelibrary.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.andrewkingmarshall.videogamelibrary.database.realmObjects.VideoGame
import com.andrewkingmarshall.videogamelibrary.util.SingleLiveEvent

class MainActivityCoroutineViewModel @ViewModelInject constructor(

): ViewModel() {

    val showError = SingleLiveEvent<String>()

    val gameLiveData = MutableLiveData<List<VideoGame>>()

    fun onGetAllGamesClicked() {
        showError.value = "onGetAllGamesClicked"
    }

    fun onGetMultiplayerGamesClicked() {
        showError.value = "onGetMultiplayerGamesClicked"
    }

    fun onGetGamesSortedAlphabeticallyClicked() {
        showError.value = "onGetGamesSortedAlphabeticallyClicked"
    }

    fun onGetGamesBySpecificDeveloperClicked() {
        showError.value = "onGetGamesBySpecificDeveloperClicked"
    }

    fun onGetGamesBySpecificReleaseYearClicked() {
        showError.value = "onGetGamesBySpecificReleaseYearClicked"
    }

    fun onGetGamesInServerOrderClicked() {
        showError.value = "onGetGamesInServerOrderClicked"
    }

    fun onGetGamesAndMediaInfoClicked() {
        showError.value = "onGetGamesAndMediaInfoClicked"
    }

}