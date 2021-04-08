package com.andrewkingmarshall.videogamelibrary.repository

import com.andrewkingmarshall.videogamelibrary.database.VideoGameDao
import com.andrewkingmarshall.videogamelibrary.database.models.VideoGame
import com.andrewkingmarshall.videogamelibrary.network.dtos.MediaDto
import com.andrewkingmarshall.videogamelibrary.network.dtos.VideoGameDto
import com.andrewkingmarshall.videogamelibrary.network.service.ApiService
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VideoGameRepositoryRoom @Inject constructor(
    private val apiService: ApiService,
    private val videoGameDao: VideoGameDao,
){



    /**
     * This will pull down all the Ids, then simultaneously pull down all the VideoGameDtos and MediaDtos
     * so we can completely refresh the database.
     */
    private suspend fun refreshVideoGameData() {

        withContext(Dispatchers.IO) {

            try {
                // Get all the game Ids
                val gameIds = apiService.getVideoGameIdsSuspend().gameIds

                // Get ready to get all the games and media in parallel
                val deferredGameList = ArrayList<Deferred<VideoGameDto>>()
                val deferredMediaList = ArrayList<Deferred<MediaDto>>()
                gameIds.forEach { gameId ->
                    deferredGameList.add(async { apiService.getVideoGameSuspend(gameId) })
                    deferredMediaList.add(async { apiService.getVideoGameMediaSuspend(gameId) })
                }

                // Make the network calls to pull this all down
                val gameList = deferredGameList.awaitAll()
                val mediaList = deferredMediaList.awaitAll()

                // Once we get all the Game and Media Dtos, go through the games and look for a matching Media
                gameList.forEach {gameDto ->
                    gameDto.mediaInfo = mediaList.firstOrNull { it.gameId == gameDto.id }
                    videoGameDao.insertVideoGame(VideoGame(gameDto))
                }

            } catch (cause: Exception) {
                throw GameRefreshError("Unable to refresh games", cause)
            }
        }
    }

}
