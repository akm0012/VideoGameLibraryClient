package com.andrewkingmarshall.videogamelibrary.viewmodel

import android.app.Application
import com.andrewkingmarshall.videogamelibrary.network.dtos.VideoGameDto
import com.andrewkingmarshall.videogamelibrary.repository.VideoGameRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.util.concurrent.Callable


@RunWith(MockitoJUnitRunner::class)
class MainActivityViewModelTest {

    @Mock
    lateinit var mockContext: Application

    @Mock
    lateinit var mockRepository: VideoGameRepository

    lateinit var viewModelUnderTest: MainActivityViewModel

    val mockGame1 = VideoGameDto(
        id = 1,
        name = "Test Game 1",
        description = "A test game 1.",
        isMultiPlayer = false,
        dateReleased = "July 17, 1990",
        developerStudio = "Andrew Marshall's Studio",
        mediaInfo = null
    )

    @Before
    @Throws(java.lang.Exception::class)
    fun setUp() {
        viewModelUnderTest = MainActivityViewModel(mockContext)
        viewModelUnderTest.videoGameRepository = mockRepository

        RxAndroidPlugins.setInitMainThreadSchedulerHandler { t: Callable<Scheduler?>? -> Schedulers.trampoline() }
    }

    @Test
    @Throws(Exception::class)
    fun onGetAllGamesClickedTest() {

        Mockito.`when`(mockRepository.getAllVideoGames()).thenReturn(Observable.just(mockGame1))

        val expected = mockGame1

        // Trigger UI input
        viewModelUnderTest.onGetAllGamesClicked()

        val actual = viewModelUnderTest.videoGameLiveData.value

        Assert.assertEquals(expected, actual)
    }

}