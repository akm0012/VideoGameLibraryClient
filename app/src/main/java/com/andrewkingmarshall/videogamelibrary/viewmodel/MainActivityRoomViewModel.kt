package com.andrewkingmarshall.videogamelibrary.viewmodel

import androidx.lifecycle.*
import com.andrewkingmarshall.videogamelibrary.database.models.VideoGame
import com.andrewkingmarshall.videogamelibrary.repository.GameRefreshError
import com.andrewkingmarshall.videogamelibrary.repository.VideoGameRepositoryRoom
import com.andrewkingmarshall.videogamelibrary.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityRoomViewModel @Inject constructor(
    private val handle: SavedStateHandle,
    private val repository: VideoGameRepositoryRoom
) : ViewModel() {

    val showError = SingleLiveEvent<String>()

   private val _gameLiveData = MutableLiveData<List<VideoGame>>()
   val gameLiveData : LiveData<List<VideoGame>> = _gameLiveData

    fun onGetAllGamesClicked() {
        viewModelScope.launch {
            try {
//                _gameLiveData.value = repository.getAllVideoGames()
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


}