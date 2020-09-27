package com.andrewkingmarshall.videogamelibrary.network.service

import com.andrewkingmarshall.videogamelibrary.network.dtos.MediaDto
import com.andrewkingmarshall.videogamelibrary.network.dtos.VideoGameDto
import com.andrewkingmarshall.videogamelibrary.network.dtos.VideoGameIdListDto
import io.reactivex.Observable
import retrofit2.Retrofit
import javax.inject.Inject

class ApiService @Inject constructor(retrofit: Retrofit) {

    private var apiServiceInterface: ApiServiceInterface =
        retrofit.create(ApiServiceInterface::class.java)

    fun getVideoGameIds(): Observable<VideoGameIdListDto> {
        return apiServiceInterface.getVideoGameIds()
    }

    fun getVideoGame(gameId: Int): Observable<VideoGameDto> {
        return apiServiceInterface.getVideoGame(gameId)
    }

    fun getVideoGameMedia(gameId: Int): Observable<MediaDto> {
        return apiServiceInterface.getVideoGameMedia(gameId)
    }

}