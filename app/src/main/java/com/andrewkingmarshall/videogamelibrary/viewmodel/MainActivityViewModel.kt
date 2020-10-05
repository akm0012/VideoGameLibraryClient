package com.andrewkingmarshall.videogamelibrary.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.andrewkingmarshall.videogamelibrary.database.clearRealm
import com.andrewkingmarshall.videogamelibrary.database.realmObjects.VideoGame
import com.andrewkingmarshall.videogamelibrary.network.dtos.VideoGameDto
import com.andrewkingmarshall.videogamelibrary.repository.VideoGameRepository
import com.andrewkingmarshall.videogamelibrary.util.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.realm.Realm
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat


class MainActivityViewModel @ViewModelInject constructor(
    private val videoGameRepository: VideoGameRepository,
    private val realm: Realm
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val videoGameLiveData = MutableLiveData<List<VideoGameDto>>()

    // todo: VideoGame would map into a Domain Model. To break the layer: api/data / domainModel
    val videoGameRealmLiveData = MutableLiveData<List<VideoGame>>()

    val showError = SingleLiveEvent<String>()

    init {
        initGameLiveData()
    }

    fun clearRealmClicked() {
        clearRealm()
    }

    private val errorListener = object :
        VideoGameRepository.ErrorListener {
        override fun onError(e: Throwable) {
            // postValue lets you call this from a background thread
            showError.postValue(e.localizedMessage)
        }
    }

    private fun initGameLiveData() {
        compositeDisposable.add(
            videoGameRepository.getAllVideoGamesSavedInRealm(realm, errorListener)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { games ->
                    videoGameRealmLiveData.value = games
                }
        )
    }

    fun onUpdateGameButtonClicked() {
        videoGameRepository.refreshGameLibrary(errorListener)
    }

    // region Old Work
    fun onGetAllGamesClicked() {
        compositeDisposable.add(
            videoGameRepository.getAllVideoGames()
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { games ->
                    videoGameLiveData.value = games
                }
        )
    }

    fun onGetMultiPlayerGamesClicked() {
        compositeDisposable.add(
            videoGameRepository.getAllVideoGames()
                .filter { it.isMultiPlayer }
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { games ->
                    videoGameLiveData.value = games
                }
        )
    }

    fun onGetGamesSortedAlphabeticallyClicked() {
        compositeDisposable.add(
            videoGameRepository.getAllVideoGames()
                .toSortedList { g1, g2 -> g1.name.compareTo(g2.name) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { games ->
                    videoGameLiveData.value = games
                }
        )
    }

    fun onGetGamesBySpecificDeveloperClicked(developerName: String) {
        compositeDisposable.add(
            videoGameRepository.getAllVideoGames()
                .filter { it.developerStudio == developerName }
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { games ->
                    videoGameLiveData.value = games
                }
        )
    }

    fun onGetGamesBySpecificReleaseYearClicked(releaseYear: String) {
        compositeDisposable.add(
            videoGameRepository.getAllVideoGames()
                .filter {
                    val releaseDateTime: DateTime =
                        DateTimeFormat.forPattern("MMMM dd, yyyy").parseDateTime(it.dateReleased)
                    releaseDateTime.year == releaseYear.toInt()
                }
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { games ->
                    videoGameLiveData.value = games
                }
        )
    }

    fun onGetGamesInServerOrderClicked() {
        compositeDisposable.add(
            videoGameRepository.getAllVideoGameIds()
                .concatMapEager { videoGameRepository.getVideoGameDetails(it) }
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { games ->
                    videoGameLiveData.value = games
                }
        )
    }

    fun onGetGamesAndMediaInfoClicked() {
        compositeDisposable.add(videoGameRepository.getAllVideoGames()
            .flatMap { gameDto ->
                videoGameRepository.getVideoGameMediaInfo(gameDto.id)
                    .map {
                        gameDto.mediaInfo = it
                        gameDto
                    }
            }
            .toList()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { games ->
                videoGameLiveData.value = games
            }
        )
    }
    //endregion

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
        realm.close()
    }
}

