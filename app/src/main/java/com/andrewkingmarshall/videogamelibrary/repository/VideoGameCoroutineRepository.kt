package com.andrewkingmarshall.videogamelibrary.repository

import com.andrewkingmarshall.videogamelibrary.network.dtos.VideoGameDto
import com.andrewkingmarshall.videogamelibrary.network.service.ApiService
import kotlinx.coroutines.*
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.system.measureTimeMillis

@Singleton
class VideoGameCoroutineRepository @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun getAllVideoGamesAsync(): List<VideoGameDto> {

        var awaitAll: List<VideoGameDto>

        withContext(Dispatchers.IO) {
            try {
                // Get all the game Ids
                val videoGameIds = apiService.getVideoGameIdsSuspend().gameIds

                val deferredGameList = ArrayList<Deferred<VideoGameDto>>()
                videoGameIds.forEach { gameId ->
                    deferredGameList.add(async { apiService.getVideoGameSuspend(gameId) })
                }

                awaitAll = deferredGameList.awaitAll()

            } catch (cause: Exception) {
                throw GameRefreshError("Unable to refresh Games", cause)
            }
        }

        return awaitAll
    }

    suspend fun getAllVideoGames(): List<VideoGameDto> {
        try {
            // Get all the game Ids
            val videoGameIds = apiService.getVideoGameIdsSuspend().gameIds

            val gameList = ArrayList<VideoGameDto>()
            videoGameIds.forEach { gameId ->
                val gameDto = apiService.getVideoGameSuspend(gameId)
                gameList.add(gameDto)
            }

            return gameList

        } catch (cause: Exception) {
            throw GameRefreshError("Unable to refresh Games", cause)
        }
    }

    suspend fun getAllGamesWithMedia(): List<VideoGameDto> {
        try {

            // Get all the game Ids
            val videoGameIds = apiService.getVideoGameIdsSuspend().gameIds

            val gameList = ArrayList<VideoGameDto>()

            videoGameIds.forEach { gameId ->

                val gameDto = apiService.getVideoGameSuspend(gameId)
                val mediaDto = apiService.getVideoGameMediaSuspend(gameId)

                gameDto.mediaInfo = mediaDto

                gameList.add(gameDto)
            }

            return gameList

        } catch (cause: Exception) {
            throw GameRefreshError("Unable to refresh Games", cause)
        }
    }
}

class GameRefreshError(message: String, cause: Throwable?) : Throwable(message, cause)