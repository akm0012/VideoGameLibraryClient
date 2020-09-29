package com.andrewkingmarshall.videogamelibrary.repository

import android.annotation.SuppressLint
import com.andrewkingmarshall.videogamelibrary.database.realmObjects.VideoGame
import com.andrewkingmarshall.videogamelibrary.extensions.save
import com.andrewkingmarshall.videogamelibrary.network.dtos.MediaDto
import com.andrewkingmarshall.videogamelibrary.network.dtos.VideoGameDto
import com.andrewkingmarshall.videogamelibrary.network.service.ApiService
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VideoGameRepository @Inject constructor(
    private val apiService: ApiService
) {

    fun getAllVideoGamesSavedInRealm(realm: Realm): Flowable<List<VideoGame>> {

        refreshGameLibrary()

        return realm.where(VideoGame::class.java)
            .sort("id")
            .findAllAsync()
            .asFlowable()
            .filter { it.isLoaded }
            .map { realm.copyFromRealm(it) }
    }

    @SuppressLint("CheckResult")
    fun refreshGameLibrary() {

        apiService.getVideoGameIds()
            .subscribeOn(Schedulers.io())
            .flatMapIterable { it.gameIds }
            .flatMap { apiService.getVideoGame(it) }
            .flatMap { gameDto ->
                apiService.getVideoGameMedia(gameDto.id)
                    .map {
                        gameDto.mediaInfo = it
                        gameDto
                    }
            }
            .map { completeGameDto -> VideoGame(completeGameDto) }
            .toList()
            .subscribe(
                { videoGameList -> videoGameList.save() },
                { error -> Timber.e(error, "Opps!") }
            )
    }

    // region Old work

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

    //endregion
}