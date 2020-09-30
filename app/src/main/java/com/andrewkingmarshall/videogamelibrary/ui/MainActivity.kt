package com.andrewkingmarshall.videogamelibrary.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.andrewkingmarshall.videogamelibrary.R
import com.andrewkingmarshall.videogamelibrary.extensions.toast
import com.andrewkingmarshall.videogamelibrary.network.service.ApiService
import com.andrewkingmarshall.videogamelibrary.viewmodel.MainActivityViewModel
import com.jakewharton.rxbinding2.view.RxView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
@SuppressLint("CheckResult")
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var apiService: ApiService

    lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        init()
    }

    private fun init() {

        viewModel =
            ViewModelProvider(this).get(MainActivityViewModel::class.java)

        // Listen for Errors
        viewModel.showError.observe(this, {
            it.toast(this)
        })

        // Listen for the Game Data to change
        viewModel.videoGameLiveData.observe(this, { gameList ->
            Toast.makeText(this, "New Game List in LogCat!", Toast.LENGTH_SHORT).show()

            if (gameList == null || gameList.isEmpty()) {
                Timber.tag("GameTag").i("[]")
            }

            gameList.forEach {
                Timber.tag("GameTag").i("$it")
            }
        })

        viewModel.videoGameRealmLiveData.observe(this, { gameList ->
            Toast.makeText(this, "New Realm Game List in LogCat!", Toast.LENGTH_SHORT).show()

            if (gameList == null || gameList.isEmpty()) {
                Timber.tag("GameTag").i("[]")
            }

            gameList.forEach {
                Timber.tag("GameTag").i("$it")
            }
        })

        clearRealmButton.setOnClickListener { viewModel.clearRealmClicked() }

        // Listen for button clicks with a 1 second delay so we don't spam the server
        RxView.clicks(updateGamesButton)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                viewModel.onUpdateGameButtonClicked()
            }

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