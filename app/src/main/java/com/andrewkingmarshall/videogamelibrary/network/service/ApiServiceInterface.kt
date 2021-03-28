package com.andrewkingmarshall.videogamelibrary.network.service

import com.andrewkingmarshall.videogamelibrary.network.dtos.MediaDto
import com.andrewkingmarshall.videogamelibrary.network.dtos.VideoGameDto
import com.andrewkingmarshall.videogamelibrary.network.dtos.VideoGameIdListDto
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiServiceInterface {

    companion object {
        const val GAME_ID = "gameId"
    }

    @GET("VideoGame/ids")
    fun getVideoGameIds(): Observable<VideoGameIdListDto>

    @GET("VideoGame/{gameId}")
    fun getVideoGame(@Path(GAME_ID) gameId: Int): Observable<VideoGameDto>

    @GET("VideoGame/Media/{gameId}")
    fun getVideoGameMedia(@Path(GAME_ID) gameId: Int): Observable<MediaDto>

    @GET("VideoGame/ids")
    suspend fun getVideoGameIdsSuspend(): VideoGameIdListDto

    @GET("VideoGame/{gameId}")
    suspend fun getVideoGameSuspend(@Path(GAME_ID) gameId: Int): VideoGameDto

    @GET("VideoGame/Media/{gameId}")
    suspend fun getVideoGameMediaSuspend(@Path(GAME_ID) gameId: Int): MediaDto
}