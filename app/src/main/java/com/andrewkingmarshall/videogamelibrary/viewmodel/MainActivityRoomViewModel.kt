package com.andrewkingmarshall.videogamelibrary.viewmodel

import androidx.lifecycle.*
import com.andrewkingmarshall.videogamelibrary.database.models.VideoGame
import com.andrewkingmarshall.videogamelibrary.repository.GameDeletionError
import com.andrewkingmarshall.videogamelibrary.repository.GameRefreshError
import com.andrewkingmarshall.videogamelibrary.repository.VideoGameRepositoryRoom
import com.andrewkingmarshall.videogamelibrary.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.internal.notifyAll
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.lang.Exception
import javax.inject.Inject

/**
 * Where lots of the lessons learned came from
 *
 * https://www.youtube.com/watch?v=B8ppnjGPAGE
 */
@HiltViewModel
class MainActivityRoomViewModel @Inject constructor(
    private val repository: VideoGameRepositoryRoom
) : ViewModel() {

    val showError = SingleLiveEvent<String>()

    private val _oneShotGameData = MutableLiveData<List<VideoGame>>()
    val oneShotGameData: LiveData<List<VideoGame>> = _oneShotGameData

    var gameData: LiveData<List<VideoGame>> = repository.videoGameFlow
        // Other functions could be done here
        .asLiveData()


    fun onGetAllGamesClicked() {
        viewModelScope.launch {
            try {
                repository.refreshVideoGameData()
            } catch (cause: GameRefreshError) {
                showError.value = cause.message
            }
        }
    }

    fun onGetMultiplayerGamesClicked() {
        viewModelScope.launch {
            repository.videoGameFlow
                .map { gameList -> gameList.filter { it.isMultiPlayer } }
                .collect {
                    _oneShotGameData.value = it
                }
        }
    }

    fun onGetGamesSortedAlphabeticallyClicked() {
        viewModelScope.launch {
            repository.videoGameFlow
                .map { gameList -> gameList.sortedBy { it.name } }
                .collect {
                    _oneShotGameData.value = it
                }
        }
    }

    fun onGetGamesBySpecificDeveloperClicked() {
        viewModelScope.launch {
            repository.videoGameFlow
                .map { gameList -> gameList.filter { it.developerStudio == "Nintendo" } }
                .collect {
                    _oneShotGameData.value = it
                }
        }
    }

    fun onGetGamesBySpecificReleaseYearClicked(releaseYear: Int) {
        viewModelScope.launch {
            repository.videoGameFlow
                .map { gameList ->
                    gameList.filter {
                        if (it.dateReleased.isNotBlank()) {

                            val releaseDateTime: DateTime =
                                DateTimeFormat.forPattern("MMMM dd, yyyy")
                                    .parseDateTime(it.dateReleased)

                            releaseDateTime.year == releaseYear
                        } else {
                            true
                        }
                    }
                }
                .collect {
                    _oneShotGameData.value = it
                }
        }
    }

    fun onGetGamesInServerOrderClicked() {
        viewModelScope.launch {
            repository.videoGameFlow
                .map { gameList -> gameList.sortedBy { it.id } }
                .collect {
                    _oneShotGameData.value = it
                }
        }
    }

    fun onInsertRandomGameClicked() {
        viewModelScope.launch {
            repository.saveRandomGame()
        }
    }

    fun onDeleteAllGamesClicked() {
        viewModelScope.launch {
            try {
                repository.deleteAllGames()
            } catch (cause: GameDeletionError) {
                showError.value = cause.message
            }
        }
    }

}