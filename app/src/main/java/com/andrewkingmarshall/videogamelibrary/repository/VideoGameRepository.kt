package com.andrewkingmarshall.videogamelibrary.repository

import com.andrewkingmarshall.videogamelibrary.database.realmObjects.VideoGame
import com.andrewkingmarshall.videogamelibrary.network.dtos.MediaDto
import com.andrewkingmarshall.videogamelibrary.network.dtos.VideoGameDto
import com.andrewkingmarshall.videogamelibrary.network.service.ApiService
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VideoGameRepository @Inject constructor(
    private val apiService: ApiService
) {

    fun getAllVideoGamesSavedInRealm(realm: Realm): Flowable<List<VideoGame>> {

        return realm.where(VideoGame::class.java)
            .findAllAsync()
            .asFlowable()
            .filter { it.isLoaded }
            .map { realm.copyFromRealm(it) }
    }

    fun getAllVideoGames(): Observable<VideoGameDto> {

        // Todo: this could be cached for better performance

        return apiService.getVideoGameIds()
            .subscribeOn(Schedulers.io())
            .flatMapIterable { it.gameIds }
            .flatMap { apiService.getVideoGame(it) }
    }

    fun getAllVideoGameIds(): Observable<Int> {

        // Todo: this could be cached for better performance

        return apiService.getVideoGameIds()
            .subscribeOn(Schedulers.io())
            .flatMapIterable { it.gameIds }
    }

    fun getVideoGameDetails(gameId: Int): Observable<VideoGameDto> {

        // Todo: this could be cached for better performance

        return apiService.getVideoGame(gameId)
            .subscribeOn(Schedulers.io())
    }

    fun getAllVideoGamesMedia(): Observable<MediaDto> {

        // Todo: this could be cached for better performance

        return apiService.getVideoGameIds()
            .subscribeOn(Schedulers.io())
            .flatMapIterable { it.gameIds }
            .flatMap { apiService.getVideoGameMedia(it) }
    }

    fun getVideoGameMediaInfo(gameId: Int): Observable<MediaDto> {
        // Todo: this could be cached for better performance

        return apiService.getVideoGameMedia(gameId)
            .subscribeOn(Schedulers.io())
    }

}