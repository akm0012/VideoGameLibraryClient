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

    private val videoGameLiveData = MutableLiveData<List<VideoGameDto>>()

    init {
        Injector.obtain().inject(this)
    }

    fun getAllVideoGames() : LiveData<List<VideoGameDto>>{

        compositeDisposable.add(
            videoGameRepository.getAllVideoGames()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    games -> videoGameLiveData.value = games
                }
        )

        return videoGameLiveData
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}

