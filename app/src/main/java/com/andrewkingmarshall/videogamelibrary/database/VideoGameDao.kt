package com.andrewkingmarshall.videogamelibrary.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.andrewkingmarshall.videogamelibrary.database.models.VideoGame
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoGameDao {

    @Query("SELECT * FROM videogame")
    fun getAllVideoGames(): Flow<List<VideoGame>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideoGames(videoGames: List<VideoGame>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideoGame(videoGame: VideoGame)
}