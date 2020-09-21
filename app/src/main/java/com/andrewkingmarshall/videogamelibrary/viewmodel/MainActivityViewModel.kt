package com.andrewkingmarshall.videogamelibrary.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.andrewkingmarshall.videogamelibrary.inject.Injector
import com.andrewkingmarshall.videogamelibrary.network.dtos.MediaDto
import com.andrewkingmarshall.videogamelibrary.network.dtos.VideoGameDto
import com.andrewkingmarshall.videogamelibrary.repository.VideoGameRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.util.*
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

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}

