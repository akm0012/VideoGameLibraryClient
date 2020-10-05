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
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VideoGameRepository @Inject constructor(
    private val apiService: ApiService
    // Todo: Pass in observable property to refresh data, then you can trottle it so you only get new api data every 5 min
) {

    interface ErrorListener {
        fun onError(e: Throwable)
    }

    fun getAllVideoGamesSavedInRealm(
        realm: Realm,
        errorListener: ErrorListener? = null
    ): Flowable<List<VideoGame>> {

//        refreshGameLibrary(errorListener)

        // todo create data wrapper object
        // todo: Find a way to combine the data stream from refreshGameLibrary so you could emit errors here

        return realm.where(VideoGame::class.java)
            .sort("id")
            .findAllAsync()
            .asFlowable()
            .filter { it.isLoaded }
            .map { realm.copyFromRealm(it) }
    }

    @SuppressLint("CheckResult")
    fun refreshGameLibrary(errorListener: ErrorListener? = null) {

        val updateStartTime = System.currentTimeMillis()
        Timber.i("Starting to refresh data.")

        // Todo: could use zip to get the VideoGameMedia at the same time: inputs: Map of Id to VideoGame and Map of Id to Media

        // Use HashMap with gameId as the key

//        val videoGameDtoMap = HashMap<Int, VideoGameDto>()
//        val videoGameMediaDtoMap = HashMap<Int, MediaDto>()
//
//        apiService.getVideoGameIds()
//            .flatMapIterable { it.gameIds }
//            .flatMap { videoGameId ->
//                apiService.getVideoGame(videoGameId)
//                    .map { gameDto -> videoGameDtoMap.put(videoGameId, gameDto) }
//            }
//            .flatMap { videoGameId ->
//                val gameId = videoGameId.id
//                apiService.getVideoGameMedia(gameId)
//                    .map { mediaDto -> videoGameMediaDtoMap.put(gameId, mediaDto) }
//            }
//            .subscribeOn(Schedulers.io()) // Do this work on a background IO thread
//            .subscribe(
//                {
//                    val videoGameList = ArrayList<VideoGame>()
//                    for (gameDto in videoGameDtoMap.entries) {
//                        videoGameList.add(VideoGame(gameDto.value, videoGameMediaDtoMap[gameDto.value.id]))
//                    }
//                    videoGameList.save()
//
//                    Timber.d("Video Games refreshed! It took ${System.currentTimeMillis() - updateStartTime} millis")
//                },
//                { error -> // Run this code when there are any errors
//                    run {
//                        Timber.e(error, "Opps!")
//                        errorListener?.onError(error)
//                    }
//                }
//            )


        apiService.getVideoGameIds() // Get the Ids of all the games
            .flatMapIterable { it.gameIds } // Turn the marbles into ints representing the Game Ids
            .flatMap {
                // Get both the VideoGameDto and the MediaDto at the same time, and then zip the result
                return@flatMap Observable.zip(
                    apiService.getVideoGame(it),
                    apiService.getVideoGameMedia(it),
                    BiFunction { gameDto, mediaDto ->
                        return@BiFunction VideoGame(
                            gameDto,
                            mediaDto
                        ) // Create a VideoGame object with both the Game and Media Dto
                    })
            }
            .toList() // Turn marbles into a list
            .map { it.save() } // Save Video Games to Realm
            .subscribeOn(Schedulers.io()) // Do this work on a background IO thread
            .subscribe(
                { Timber.d("Video Games refreshed! It took ${System.currentTimeMillis() - updateStartTime} millis") }, // Log when things work
                { error -> // Run this code when there are any errors
                    run {
                        Timber.e(error, "Opps!")
                        errorListener?.onError(error)
                    }
                }
            )

//        apiService.getVideoGameIds() // Get the Ids of all the games
//            .flatMapIterable { it.gameIds } // Turn the marbles into ints representing the Game Ids
//            .flatMap { apiService.getVideoGame(it) } // Get all the Video Game Dtos for every ID
//            .flatMap { gameDto ->
//                apiService.getVideoGameMedia(gameDto.id) // Get the Media for the GameDto
//                    .map {
//                        gameDto.mediaInfo =
//                            it // Add the Media info to the Game Dto. The Game Dto now has all the info
//                        gameDto
//                    }
//            }
//            .map { completeGameDto -> VideoGame(completeGameDto) } // Create a Realm "VideoGame" from the complete Game Dto
//            .toList() // Turn the marbles into a List
//            .map { it.save() } // Save the Video Games to Realm
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