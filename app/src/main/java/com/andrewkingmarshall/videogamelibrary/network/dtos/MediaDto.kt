package com.andrewkingmarshall.videogamelibrary.network.dtos

data class MediaDto(

    val id: Int,

    val name: String,

    val description: String,

    val isMultiPlayer: Boolean,

    val dateReleased: String,

    val developerStudio: String,
)
