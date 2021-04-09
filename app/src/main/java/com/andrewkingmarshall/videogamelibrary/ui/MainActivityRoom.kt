package com.andrewkingmarshall.videogamelibrary.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.andrewkingmarshall.videogamelibrary.R
import com.andrewkingmarshall.videogamelibrary.database.AppDatabase
import com.andrewkingmarshall.videogamelibrary.database.models.VideoGame
import com.andrewkingmarshall.videogamelibrary.extensions.toast
import com.andrewkingmarshall.videogamelibrary.viewmodel.MainActivityRoomViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main_room.*
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MainActivityRoom : AppCompatActivity() {

    val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "VideoGame-Database"
        ).build()
    }

    lateinit var viewModel: MainActivityRoomViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_room)

        viewModel = ViewModelProvider(this).get(MainActivityRoomViewModel::class.java)

        viewModel.showError.observe(this, { it.toast(this) })

        viewModel.oneShotGameData.observe(this, { gameList ->
            Toast.makeText(this, "New Game List in LogCat!", Toast.LENGTH_SHORT).show()

            if (gameList.isNullOrEmpty()) {
                Timber.tag("GameTag").d("[]")
            }

            gameList.forEach {
                Timber.tag("GameTag").d("$it")
            }
        })

        viewModel.gameData.observe(this, { gameList ->
            Toast.makeText(this, "New Game List in LogCat!", Toast.LENGTH_SHORT).show()

            if (gameList.isNullOrEmpty()) {
                Timber.tag("GameTag").d("[]")
            }

            gameList.forEach {
                Timber.tag("GameTag").d("$it")
            }
        })

        getAllGamesButton.setOnClickListener { viewModel.onGetAllGamesClicked() }
        getMultiPlayerGamesButton.setOnClickListener { viewModel.onGetMultiplayerGamesClicked() }
        getGamesSortedAlphaButton.setOnClickListener { viewModel.onGetGamesSortedAlphabeticallyClicked() }
        getGamesBySpecificDeveloperButton.setOnClickListener { viewModel.onGetGamesBySpecificDeveloperClicked() }
        getGamesBySpecificReleaseYearButton.setOnClickListener { viewModel.onGetGamesBySpecificReleaseYearClicked() }
//        getGamesInServerOrderButton.setOnClickListener { viewModel.onGetGamesInServerOrderClicked() }
        getGamesInServerOrderButton.setOnClickListener {

            lifecycleScope.launch {
                db.videoGameDao().insertVideoGame(
                    VideoGame(
                        id = (1..10_0000).shuffled().first(),
                        name = "A video Game with a new ID",
                        description = "A Description"
                    )
                )
            }
        }
    }
}