package com.andrewkingmarshall.videogamelibrary.repository

import com.andrewkingmarshall.videogamelibrary.network.dtos.VideoGameDto
import com.andrewkingmarshall.videogamelibrary.network.service.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VideoGameCoroutineRepository @Inject constructor(
    private val apiService: ApiService
) {

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

class GameRefreshError(message: String, cause: Throwable?) :Throwable(message, cause)