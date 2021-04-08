package com.andrewkingmarshall.videogamelibrary.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.andrewkingmarshall.videogamelibrary.R
import com.andrewkingmarshall.videogamelibrary.extensions.toast
import com.andrewkingmarshall.videogamelibrary.viewmodel.MainActivityViewModelRx
import com.jakewharton.rxbinding2.view.RxView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
@SuppressLint("CheckResult")
class MainActivity : AppCompatActivity() {

    lateinit var viewModelRx: MainActivityViewModelRx

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        init()
    }

    private fun init() {

        viewModelRx =
            ViewModelProvider(this).get(MainActivityViewModelRx::class.java)

        // Listen for Errors
        viewModelRx.showError.observe(this, {
            it.toast(this)
        })

        // Listen for the Game Data to change
        viewModelRx.videoGameLiveData.observe(this, { gameList ->
            Toast.makeText(this, "New Game List in LogCat!", Toast.LENGTH_SHORT).show()

            if (gameList == null || gameList.isEmpty()) {
                Timber.tag("GameTag").i("[]")
            }

            gameList.forEach {
                Timber.tag("GameTag").i("$it")
            }
        })

        viewModelRx.videoGameRealmLiveData.observe(this, { gameList ->
            Toast.makeText(this, "New Realm Game List in LogCat!", Toast.LENGTH_SHORT).show()

            if (gameList == null || gameList.isEmpty()) {
                Timber.tag("GameTag").i("[]")
            }

            gameList.forEach {
                Timber.tag("GameTag").i("$it")
            }
        })

        // Listen for button clicks with a 1 second delay so we don't spam the server
        RxView.clicks(updateGamesButton)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                viewModelRx.onUpdateGameButtonClicked()
            }

        RxView.clicks(getAllGamesButton)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                viewModelRx.onGetAllGamesClicked()
            }

        RxView.clicks(getMultiPlayerGamesButton)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                viewModelRx.onGetMultiPlayerGamesClicked()
            }

        RxView.clicks(getGamesSortedAlphaButton)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                viewModelRx.onGetGamesSortedAlphabeticallyClicked()
            }

        RxView.clicks(getGamesBySpecificDeveloperButton)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                viewModelRx.onGetGamesBySpecificDeveloperClicked("Nintendo")
            }

        RxView.clicks(getGamesBySpecificReleaseYearButton)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                viewModelRx.onGetGamesBySpecificReleaseYearClicked("2012")
            }

        RxView.clicks(getGamesInServerOrderButton)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                viewModelRx.onGetGamesInServerOrderClicked()
            }

        RxView.clicks(getGamesAndMediaInfoButton)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                viewModelRx.onGetGamesAndMediaInfoClicked()
            }
    }
}