package com.andrewkingmarshall.videogamelibrary.repository

import android.annotation.SuppressLint
import com.andrewkingmarshall.videogamelibrary.database.realmObjects.VideoGame
import com.andrewkingmarshall.videogamelibrary.extensions.save
import com.andrewkingmarshall.videogamelibrary.extensions.saveAsync
import com.andrewkingmarshall.videogamelibrary.extensions.saveRx
import com.andrewkingmarshall.videogamelibrary.network.dtos.MediaDto
import com.andrewkingmarshall.videogamelibrary.network.dtos.VideoGameDto
import com.andrewkingmarshall.videogamelibrary.network.service.ApiService
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VideoGameRepository @Inject constructor(
    private val apiService: ApiService
) {

    interface ErrorListener {
        fun onError(e: Throwable)
    }

    fun getAllVideoGamesSavedInRealm(
        realm: Realm,
        errorListener: ErrorListener? = null
    ): Flowable<List<VideoGame>> {

//        refreshGameLibrary(errorListener)

        return realm.where(VideoGame::class.java)
            .sort("id")
            .findAllAsync()
            .asFlowable()
            .filter { it.isLoaded }
            .map { realm.copyFromRealm(it) }
    }

    @SuppressLint("CheckResult")
    fun refreshGameLibrary(errorListener: ErrorListener? = null) {

        apiService.getVideoGameIds() // Get the Ids of all the games
            .subscribeOn(Schedulers.io()) // Do this work on a background IO thread
            .flatMapIterable { it.gameIds } // Turn the marbles into ints representing the Game Ids
            .flatMap { apiService.getVideoGame(it) } // Get all the Video Game Dtos for every ID
            .flatMap { gameDto ->
                apiService.getVideoGameMedia(gameDto.id) // Get the Media for the GameDto
                    .map {
                        gameDto.mediaInfo = it // Add the Media info to the Game Dto. The Game Dto now has all the info
                        gameDto
                    }
            }
            .map { completeGameDto -> VideoGame(completeGameDto) } // Create a Realm "VideoGame" from the complete Game Dto
            .toList() // Turn the marbles into a List
            .map { it.save() } // Save the Video Games to Realm
            .observeOn(AndroidSchedulers.mainThread()) // Observe the results on the Main Thread
            .subscribe(
                { Timber.d("Video Games refreshed!") }, // Log when things work
                { error -> // Run this code when there are any errors
                    run {
                        Timber.e(error, "Opps!")
                        errorListener?.onError(error)
                    }
                }
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