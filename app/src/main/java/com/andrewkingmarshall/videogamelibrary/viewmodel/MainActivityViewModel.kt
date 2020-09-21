package com.andrewkingmarshall.videogamelibrary.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.andrewkingmarshall.videogamelibrary.inject.Injector
import com.andrewkingmarshall.videogamelibrary.network.dtos.VideoGameDto
import com.andrewkingmarshall.videogamelibrary.repository.VideoGameRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {

    @Inject
    lateinit var videoGameRepository: VideoGameRepository

    private val compositeDisposable = CompositeDisposable()

    val videoGameLiveData = MutableLiveData<List<VideoGameDto>>()

    init {
        Injector.obtain().inject(this)
    }

    fun onGetAllGamesClicked() {
        compositeDisposable.add(
            videoGameRepository.getAllVideoGames()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    games -> videoGameLiveData.value = games
                }
        )
    }

    fun onGetMultiPlayerGamesClicked() {

    }

    fun onGetGamesSortedAlphabeticallyClicked() {

    }

    fun onGetGamesBySpecificDeveloperClicked(developerName: String) {

    }

    fun onGetGamesBySpecificReleaseYearClicked(releaseYear: String) {

    }

    fun onGetGamesInServerOrderClicked() {
        compositeDisposable.add(
            videoGameRepository.getAllVideoGameIds()
                .concatMapEager { videoGameRepository.getVideoGameDetails(it) }
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                        games -> videoGameLiveData.value = games
                }
        )
    }

    fun onGetGamesAndMediaInfoClicked() {

    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}

