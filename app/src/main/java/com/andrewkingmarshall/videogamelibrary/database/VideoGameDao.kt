package com.andrewkingmarshall.videogamelibrary.database

import androidx.room.*
import com.andrewkingmarshall.videogamelibrary.database.models.VideoGame
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoGameDao {

    @Query("SELECT * FROM VideoGame")
    fun getAllVideoGames(): Flow<List<VideoGame>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideoGames(videoGames: List<VideoGame>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideoGame(videoGame: VideoGame)

    @Delete
    suspend fun deleteGame(videoGame: VideoGame)

    @Delete
    suspend fun deleteGames(videoGames: List<VideoGame>)
}