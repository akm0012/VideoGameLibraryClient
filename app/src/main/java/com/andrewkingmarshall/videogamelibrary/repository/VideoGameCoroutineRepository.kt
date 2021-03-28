package com.andrewkingmarshall.videogamelibrary.repository

import com.andrewkingmarshall.videogamelibrary.network.dtos.MediaDto
import com.andrewkingmarshall.videogamelibrary.network.dtos.VideoGameDto
import com.andrewkingmarshall.videogamelibrary.network.service.ApiService
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VideoGameCoroutineRepository @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun getAllVideoGamesAsync(): List<VideoGameDto> {

        var videoGames: List<VideoGameDto>

        withContext(Dispatchers.IO) {
            try {
                // Get all the game Ids
                val videoGameIds = apiService.getVideoGameIdsSuspend().gameIds

                val deferredGameList = ArrayList<Deferred<VideoGameDto>>()
                videoGameIds.forEach { gameId ->
                    deferredGameList.add(async { apiService.getVideoGameSuspend(gameId) })
                }

                videoGames = deferredGameList.awaitAll()

            } catch (cause: Exception) {
                throw GameRefreshError("Unable to refresh Games", cause)
            }
        }

        return videoGames
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

    /**
     * This will get all the games, then get all the media in sequential order.
     *
     */
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

    /**
     *  This will get all the Games in parallel, then get all the Ids in sequential order.
     */
    suspend fun getAllGamesWithMediaAsyncOptimized_1(): List<VideoGameDto> {
        try {

            val videoGames = getAllVideoGamesAsync()
            videoGames.forEach {
                val mediaInfo = apiService.getVideoGameMediaSuspend(it.id)
                it.mediaInfo = mediaInfo
            }

            return videoGames

        } catch (cause: Exception) {
            throw GameRefreshError("Unable to refresh Games", cause)
        }
    }

    /**
     *  This will get all the Games in parallel, then get all the Ids in parallel.
     */
    suspend fun getAllGamesWithMediaAsyncOptimized_2(): List<VideoGameDto> {

        val videoGames = ArrayList<VideoGameDto>()

        withContext(Dispatchers.IO) {
            try {

                val videoGamesWithoutMedia = getAllVideoGamesAsync()
                val deferredListOfMedia = ArrayList<Deferred<MediaDto>>()
                videoGamesWithoutMedia.forEach {
                    deferredListOfMedia.add(async { apiService.getVideoGameMediaSuspend(it.id) })
                }

                val mediaList = deferredListOfMedia.awaitAll()

                videoGamesWithoutMedia.forEach { gameDto ->
                    gameDto.mediaInfo = mediaList.firstOrNull { it.gameId == gameDto.id}
                    videoGames.add(gameDto)
                }

            } catch (cause: Exception) {
                throw GameRefreshError("Unable to refresh Games", cause)
            }
        }

        return videoGames
    }

    /**
     *  This will get both the gameDtos and MediaDtos in parallel then combine at the end.
     */
    suspend fun getAllGamesWithMediaAsyncOptimized_3(): List<VideoGameDto> {

        val videoGames = ArrayList<VideoGameDto>()

        withContext(Dispatchers.IO) {
            try {
                // Get all the game Ids
                val videoGameIds = apiService.getVideoGameIdsSuspend().gameIds

                // Get ready to get all the Games and Media in parallel
                val deferredGameList = ArrayList<Deferred<VideoGameDto>>()
                val deferredMediaList = ArrayList<Deferred<MediaDto>>()
                videoGameIds.forEach { gameId ->
                    deferredGameList.add(async { apiService.getVideoGameSuspend(gameId) })
                    deferredMediaList.add(async { apiService.getVideoGameMediaSuspend(gameId) })
                }

                // Here is where network calls are kicked off
                val gameList = deferredGameList.awaitAll()
                val mediaList = deferredMediaList.awaitAll()

                // Once we get all the Game and Media Dtos, go through the games look for a matching Media
                gameList.forEach { gameDto ->
                    gameDto.mediaInfo = mediaList.firstOrNull { it.gameId == gameDto.id}
                    videoGames.add(gameDto)
                }

            } catch (cause: Exception) {
                throw GameRefreshError("Unable to refresh Games", cause)
            }
        }

        return videoGames
    }


}

class GameRefreshError(message: String, cause: Throwable?) : Throwable(message, cause)