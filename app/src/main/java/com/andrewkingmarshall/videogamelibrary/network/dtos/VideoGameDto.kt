package com.andrewkingmarshall.videogamelibrary.network.dtos

data class VideoGameDto(

    val id: Int,

    val name: String,

    val description: String,

    val isMultiPlayer: Boolean,

    val dateReleased: String,

    val developerStudio: String,
)
