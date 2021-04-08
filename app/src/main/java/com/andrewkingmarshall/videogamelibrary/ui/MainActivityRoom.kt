package com.andrewkingmarshall.videogamelibrary.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.andrewkingmarshall.videogamelibrary.R
import com.andrewkingmarshall.videogamelibrary.extensions.toast
import com.andrewkingmarshall.videogamelibrary.viewmodel.MainActivityRoomViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main_room.*
import timber.log.Timber

@AndroidEntryPoint
class MainActivityRoom : AppCompatActivity() {

    lateinit var viewModel: MainActivityRoomViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_room)

        viewModel = ViewModelProvider(this).get(MainActivityRoomViewModel::class.java)

        viewModel.showError.observe(this, { it.toast(this) })

        viewModel.gameLiveData.observe(this, { gameList ->
            Toast.makeText(this, "New Game List in LogCat!", Toast.LENGTH_SHORT).show()

            if (gameList.isNullOrEmpty()) {
                Timber.tag("GameTag").i("[]")
            }

            gameList.forEach {
                Timber.tag("GameTag").i("$it")
            }
        })

        getAllGamesButton.setOnClickListener { viewModel.onGetAllGamesClicked() }
        getMultiPlayerGamesButton.setOnClickListener { viewModel.onGetMultiplayerGamesClicked() }
        getGamesSortedAlphaButton.setOnClickListener { viewModel.onGetGamesSortedAlphabeticallyClicked() }
        getGamesBySpecificDeveloperButton.setOnClickListener { viewModel.onGetGamesBySpecificDeveloperClicked() }
        getGamesBySpecificReleaseYearButton.setOnClickListener { viewModel.onGetGamesBySpecificReleaseYearClicked() }
        getGamesInServerOrderButton.setOnClickListener { viewModel.onGetGamesInServerOrderClicked() }
    }
}