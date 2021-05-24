package com.andrewkingmarshall.videogamelibrary.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrewkingmarshall.videogamelibrary.network.dtos.VideoGameDto
import com.andrewkingmarshall.videogamelibrary.repository.GameRefreshError
import com.andrewkingmarshall.videogamelibrary.repository.VideoGameCoroutineRepository
import com.andrewkingmarshall.videogamelibrary.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainActivityCoroutineViewModel @Inject constructor(
    private val repository: VideoGameCoroutineRepository
) : ViewModel() {

    val showError = SingleLiveEvent<String>()

    val gameLiveData = MutableLiveData<List<VideoGameDto>>()

    var networkCallStartTime = 0L

    fun onGetAllGamesOnParallelThreadClicked() {
        Timber.tag("GameTag").d("Starting to get all games in parallel")
        networkCallStartTime = System.currentTimeMillis()
        viewModelScope.launch {
            try {
                gameLiveData.value = repository.getAllVideoGamesAsync()
            } catch (error: GameRefreshError) {
                showError.value = error.message
            }
        }
    }

    fun onGetAllGamesClicked() {
        Timber.tag("GameTag").d("Starting to get all games")
        networkCallStartTime = System.currentTimeMillis()
        viewModelScope.launch {
            try {
                gameLiveData.value = repository.getAllVideoGames()
            } catch (error: GameRefreshError) {
                showError.value = error.message
            }
        }
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

    fun onGetGamesAndMediaInfo_1_Clicked() {
        Timber.tag("GameTag").d("Starting to get all games and media")
        networkCallStartTime = System.currentTimeMillis()
        viewModelScope.launch {
            try {
                gameLiveData.value = repository.getAllGamesWithMedia()
            } catch (error: GameRefreshError) {
                showError.value = error.message
            }
        }
    }

    fun onGetGamesAndMediaInfo_2_Clicked() {
        Timber.tag("GameTag").d("Starting to get all games and media")
        networkCallStartTime = System.currentTimeMillis()
        viewModelScope.launch {
            try {
                gameLiveData.value = repository.getAllGamesWithMediaAsyncOptimized_1()
            } catch (error: GameRefreshError) {
                showError.value = error.message
            }
        }
    }

    fun onGetGamesAndMediaInfo_3_Clicked() {
        Timber.tag("GameTag").d("Starting to get all games and media")
        networkCallStartTime = System.currentTimeMillis()
        viewModelScope.launch {
            try {
                gameLiveData.value = repository.getAllGamesWithMediaAsyncOptimized_2()
            } catch (error: GameRefreshError) {
                showError.value = error.message
            }
        }
    }

    fun onGetGamesAndMediaInfo_4_Clicked() {
        Timber.tag("GameTag").d("Starting to get all games and media")
        networkCallStartTime = System.currentTimeMillis()
        viewModelScope.launch {
            try {
                gameLiveData.value = repository.getAllGamesWithMediaAsyncOptimized_3()
            } catch (error: GameRefreshError) {
                showError.value = error.message
            }
        }
    }


}