package com.andrewkingmarshall.videogamelibrary.repository

import com.andrewkingmarshall.videogamelibrary.database.VideoGameDao
import com.andrewkingmarshall.videogamelibrary.database.models.VideoGame
import com.andrewkingmarshall.videogamelibrary.network.dtos.MediaDto
import com.andrewkingmarshall.videogamelibrary.network.dtos.VideoGameDto
import com.andrewkingmarshall.videogamelibrary.network.service.ApiService
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VideoGameRepositoryRoom @Inject constructor(
    private val apiService: ApiService,
    private val videoGameDao: VideoGameDao,
) {

    val videoGameFlow = videoGameDao.getAllVideoGames()

    suspend fun saveRandomGame() {
        withContext(Dispatchers.IO) {

            videoGameDao.insertVideoGame(
                VideoGame(
                    id = (1..10_0000).shuffled().first(),
                    name = "A video Game with a new ID",
                    description = "A Description"
                )
            )
        }
    }

    /**
     * This will pull down all the Ids, then simultaneously pull down all the VideoGameDtos and MediaDtos
     * so we can completely refresh the database.
     */
    suspend fun refreshVideoGameData() {

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

                val gamesToSave = ArrayList<VideoGame>()

                // Once we get all the Game and Media Dtos, go through the games and look for a matching Media
                gameList.forEach { gameDto ->
                    gameDto.mediaInfo = mediaList.firstOrNull { it.gameId == gameDto.id }
                    gamesToSave.add(VideoGame(gameDto))

                    // Note: Adding the games one at a time causes the Flow to be hit many times
                    //  while things are being written
//                    videoGameDao.insertVideoGame(VideoGame(gameDto))
                }

                videoGameDao.insertVideoGames(gamesToSave)

            } catch (cause: Exception) {
                throw GameRefreshError("Unable to refresh games", cause)
            }
        }
    }

    suspend fun deleteAllGames() {

        withContext(Dispatchers.IO) {
            try {
                val allVideoGames = videoGameDao.getAllVideoGames().first()
                videoGameDao.deleteGames(allVideoGames)

            } catch (cause: Throwable) {
                throw GameDeletionError("Unable to delete games.", cause)
            }
        }
    }

}

class GameDeletionError(message: String, cause: Throwable?): Throwable(message, cause)
