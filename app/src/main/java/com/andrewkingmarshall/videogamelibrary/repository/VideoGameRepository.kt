package com.andrewkingmarshall.videogamelibrary.repository

import android.annotation.SuppressLint
import com.andrewkingmarshall.videogamelibrary.database.realmObjects.VideoGame
import com.andrewkingmarshall.videogamelibrary.extensions.save
import com.andrewkingmarshall.videogamelibrary.extensions.saveAsync
import com.andrewkingmarshall.videogamelibrary.extensions.saveRx
import com.andrewkingmarshall.videogamelibrary.network.dtos.MediaDto
import com.andrewkingmarshall.videogamelibrary.network.dtos.VideoGameDto
import com.andrewkingmarshall.videogamelibrary.network.service.ApiService
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VideoGameRepository @Inject constructor(
    private val apiService: ApiService,
    private val refreshEmitter: Observable<Boolean>,
    private val realm: Observable<Realm>
    // Todo: Pass in observable property to refresh data, then you can trottle it so you only get new api data every 5 min
) {

    val videoGameEmitter = Observable.combineLatest(refreshEmitter, realm, { refresh, realm2 ->
        realm2
    })
        .doOnNext { refreshGameLibrary() }
        .toFlowable(BackpressureStrategy.LATEST)
        .switchMap { getAllVideoGamesSavedInRealm(it) }

    interface ErrorListener {
        fun onError(e: Throwable)
    }

    private fun getAllVideoGamesSavedInRealm(
        realm: Realm,
        errorListener: ErrorListener? = null
    ): Flowable<VideoGameDomainObject> {

        refreshGameLibrary(errorListener)

        // todo create data wrapper object
        // todo: Find a way to combine the data stream from refreshGameLibrary so you could emit errors here

        return realm.where(VideoGame::class.java)
            .sort("id")
            .findAllAsync()
            .asFlowable()
            .filter { it.isLoaded }
            .map { VideoGameDomainObject.Success(realm.copyFromRealm(it)) }
    }

    @SuppressLint("CheckResult")
    private fun refreshGameLibrary(errorListener: ErrorListener? = null): Single<VideoGameDomainObject> {

        val updateStartTime = System.currentTimeMillis()
        Timber.i("Starting to refresh data.")

        return apiService.getVideoGameIds() // Get the Ids of all the games
            .flatMapIterable { it.gameIds } // Turn the marbles into ints representing the Game Ids
            .flatMap {
                // Get both the VideoGameDto and the MediaDto at the same time, and then zip the result
                return@flatMap Observable.zip(
                    apiService.getVideoGame(it),
                    apiService.getVideoGameMedia(it),
                    BiFunction { gameDto, mediaDto ->
                        Timber.d("GameId ${gameDto.id} processed")
                        return@BiFunction VideoGame(
                            gameDto,
                            mediaDto
                        ) // Create a VideoGame object with both the Game and Media Dto
                    })
            }
            .toList() // Turn marbles into a list
            .doOnSuccess { it.save() } // Save Video Games to Realm
            .map { VideoGameDomainObject.Success(it) as VideoGameDomainObject }
            .onErrorReturn { VideoGameDomainObject.Error(it) as VideoGameDomainObject }

//            .subscribeOn(Schedulers.io()) // Do this work on a background IO thread
//            .subscribe(
//                { Timber.d("Video Games refreshed! It took ${System.currentTimeMillis() - updateStartTime} millis") }, // Log when things work
//                { error -> // Run this code when there are any errors
//                    run {
//                        Timber.e(error, "Opps!")
//                        errorListener?.onError(error)
//                    }
//                }
//            )
    }

    sealed class VideoGameDomainObject {
        data class Success(
            val games: List<VideoGame>
        ) : VideoGameDomainObject()

        data class Error(
            val error: Throwable
        ) : VideoGameDomainObject()
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