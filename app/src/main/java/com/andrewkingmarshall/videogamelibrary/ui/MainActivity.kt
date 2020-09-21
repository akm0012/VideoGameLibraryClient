package com.andrewkingmarshall.videogamelibrary.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.andrewkingmarshall.videogamelibrary.R
import com.andrewkingmarshall.videogamelibrary.inject.Injector
import com.andrewkingmarshall.videogamelibrary.network.service.ApiService
import com.andrewkingmarshall.videogamelibrary.viewmodel.MainActivityViewModel
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@SuppressLint("CheckResult")
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var apiService: ApiService

    lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Injector.obtain().inject(this)

        setContentView(R.layout.activity_main)

        init()
    }

    private fun init() {

        viewModel =
            ViewModelProvider(this).get(MainActivityViewModel::class.java)

        // Listen for the Game Data to change
        viewModel.videoGameLiveData.observe(this, { gameList ->
            Toast.makeText(this, "New Game List in LogCat!", Toast.LENGTH_SHORT).show()

            gameList.forEach {
                Log.i("GameTag", "$it")
            }
        })

        // Listen for button clicks with a 1 second delay so we don't spam the server
        RxView.clicks(getAllGamesButton)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                viewModel.onGetAllGamesClicked()
            }

        RxView.clicks(getMultiPlayerGamesButton)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                viewModel.onGetMultiPlayerGamesClicked()
            }

        RxView.clicks(getGamesSortedAlphaButton)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                viewModel.onGetGamesSortedAlphabeticallyClicked()
            }

        RxView.clicks(getGamesBySpecificDeveloperButton)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                viewModel.onGetGamesBySpecificDeveloperClicked("Nintendo")
            }

        RxView.clicks(getGamesBySpecificReleaseYearButton)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                viewModel.onGetGamesBySpecificReleaseYearClicked("2012")
            }

        RxView.clicks(getGamesInServerOrderButton)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                viewModel.onGetGamesInServerOrderClicked()
            }

        RxView.clicks(getGamesAndMediaInfoButton)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                viewModel.onGetGamesAndMediaInfoClicked()
            }
    }
}