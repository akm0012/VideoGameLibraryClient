package com.andrewkingmarshall.videogamelibrary.database.realmObjects

import com.andrewkingmarshall.videogamelibrary.network.dtos.MediaDto
import com.andrewkingmarshall.videogamelibrary.network.dtos.VideoGameDto
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class VideoGame(

    @PrimaryKey
    var id: Int = 0,

    var name: String = "",

    var description: String = "",

    var isMultiPlayer: Boolean = false,

    var dateReleased: String = "",

    var developerStudio: String = "",

    var gamePosterUrl: String = "",

    var gameTrailerUrl: String = ""

) : RealmObject() {

    constructor(
        videoGameDto: VideoGameDto,
        mediaDto: MediaDto? = videoGameDto.mediaInfo
    ) : this() {

        id = videoGameDto.id
        name = videoGameDto.name
        description = videoGameDto.description
        isMultiPlayer = videoGameDto.isMultiPlayer
        dateReleased = videoGameDto.dateReleased
        developerStudio = videoGameDto.developerStudio

        // Use the Media DTO if you there, otherwise, use the data in the videoGameDto
        if (mediaDto != null) {
            gamePosterUrl = mediaDto.gamePosterUrl
            gameTrailerUrl = mediaDto.gameTrailerUrl
        } else {
            gamePosterUrl = videoGameDto.mediaInfo?.gamePosterUrl ?: ""
            gameTrailerUrl = videoGameDto.mediaInfo?.gameTrailerUrl ?: ""
        }
    }

    override fun toString(): String {
        return "VideoGame(id=$id, name='$name', description='$description', isMultiPlayer=$isMultiPlayer, dateReleased='$dateReleased', developerStudio='$developerStudio', gamePosterUrl='$gamePosterUrl', gameTrailerUrl='$gameTrailerUrl')"
    }


}
