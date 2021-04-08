package com.andrewkingmarshall.videogamelibrary.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.andrewkingmarshall.videogamelibrary.database.models.VideoGame

@Database(entities = arrayOf(VideoGame::class), version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun videoGameDao(): VideoGameDao
}