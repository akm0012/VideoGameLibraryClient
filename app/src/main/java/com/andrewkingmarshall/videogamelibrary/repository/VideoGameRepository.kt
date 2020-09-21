package com.andrewkingmarshall.videogamelibrary.repository

import com.andrewkingmarshall.videogamelibrary.inject.Injector
import com.andrewkingmarshall.videogamelibrary.network.dtos.VideoGameDto
import com.andrewkingmarshall.videogamelibrary.network.service.ApiService
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class VideoGameRepository {

    @Inject
    lateinit var apiService:ApiService

    init {
        Injector.obtain().inject(this)
    }

    fun getAllVideoGames() : Single<List<VideoGameDto>> {

        // Todo: this could be cached for better performance

        return apiService.getVideoGameIds()
            .subscribeOn(Schedulers.io())
            .flatMapIterable { it.gameIds }
            .flatMap { apiService.getVideoGame(it) }
            .toList()
    }

}