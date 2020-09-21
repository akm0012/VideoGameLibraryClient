package com.andrewkingmarshall.videogamelibrary.network.dtos

data class MediaDto(

    val mediaId: Int,

    val gameId: Int,

    val gamePosterUrl: String,

    val gameTrailerUrl: String,
)
