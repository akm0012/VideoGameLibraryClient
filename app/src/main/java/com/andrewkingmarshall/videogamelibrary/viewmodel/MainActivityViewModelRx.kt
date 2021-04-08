package com.andrewkingmarshall.videogamelibrary.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.andrewkingmarshall.videogamelibrary.database.models.VideoGame
import com.andrewkingmarshall.videogamelibrary.network.dtos.VideoGameDto
import com.andrewkingmarshall.videogamelibrary.repository.VideoGameRepositoryRx
import com.andrewkingmarshall.videogamelibrary.util.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat


class MainActivityViewModelRx @ViewModelInject constructor(
    private val videoGameRepositoryRx: VideoGameRepositoryRx,
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val videoGameLiveData = MutableLiveData<List<VideoGameDto>>()

    // todo: VideoGame would map into a Domain Model. To break the layer: api/data / domainModel
    val videoGameRealmLiveData = MutableLiveData<List<VideoGame>>()

    val showError = SingleLiveEvent<String>()

    init {
//        initGameLiveData()
    }

    private val errorListener = object :
        VideoGameRepositoryRx.ErrorListener {
        override fun onError(e: Throwable) {
            // postValue lets you call this from a background thread
            showError.postValue(e.localizedMessage)
        }
    }

//    private fun initGameLiveData() {
//        compositeDisposable.add(
//            videoGameRepositoryRx.getAllVideoGamesSavedInRealm(realm, errorListener)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe { games ->
//                    videoGameRealmLiveData.value = games
//                }
//        )
//    }

    fun onUpdateGameButtonClicked() {
        videoGameRepositoryRx.refreshGameLibrary(errorListener)
    }

    // region Old Work
    fun onGetAllGamesClicked() {
        compositeDisposable.add(
            videoGameRepositoryRx.getAllVideoGames()
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { games ->
                    videoGameLiveData.value = games
                }
        )
    }

    fun onGetMultiPlayerGamesClicked() {
        compositeDisposable.add(
            videoGameRepositoryRx.getAllVideoGames()
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
            videoGameRepositoryRx.getAllVideoGames()
                .toSortedList { g1, g2 -> g1.name.compareTo(g2.name) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { games ->
                    videoGameLiveData.value = games
                }
        )
    }

    fun onGetGamesBySpecificDeveloperClicked(developerName: String) {
        compositeDisposable.add(
            videoGameRepositoryRx.getAllVideoGames()
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
            videoGameRepositoryRx.getAllVideoGames()
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
            videoGameRepositoryRx.getAllVideoGameIds()
                .concatMapEager { videoGameRepositoryRx.getVideoGameDetails(it) }
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { games ->
                    videoGameLiveData.value = games
                }
        )
    }

    fun onGetGamesAndMediaInfoClicked() {
        compositeDisposable.add(videoGameRepositoryRx.getAllVideoGames()
            .flatMap { gameDto ->
                videoGameRepositoryRx.getVideoGameMediaInfo(gameDto.id)
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
    }
}

