package com.andrewkingmarshall.videogamelibrary.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.andrewkingmarshall.videogamelibrary.R
import com.andrewkingmarshall.videogamelibrary.extensions.toast
import com.andrewkingmarshall.videogamelibrary.viewmodel.MainActivityCoroutineViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main_coroutine.*
import timber.log.Timber

@AndroidEntryPoint
class MainActivityCoroutine : AppCompatActivity() {

    private val viewModel by lazy { ViewModelProvider(this).get(MainActivityCoroutineViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_coroutine)

        viewModel.showError.observe(this, { it.toast(this) })

        viewModel.gameLiveData.observe(this, { gameList ->
            Toast.makeText(this, "New Game List in LogCat!", Toast.LENGTH_SHORT).show()

            if (gameList.isNullOrEmpty()) {
                Timber.tag("GameTag").i("[]")
            }

            gameList.forEach {
                Timber.tag("GameTag").i("$it")
            }

            Timber.tag("GameTag")
                .d("Time taken: ${System.currentTimeMillis() - viewModel.networkCallStartTime} ms")
        })

        getAllGamesParallelButton.setOnClickListener { viewModel.onGetAllGamesOnParallelThreadClicked() }
        getAllGamesButton.setOnClickListener { viewModel.onGetAllGamesClicked() }
        getMultiPlayerGamesButton.setOnClickListener { viewModel.onGetMultiplayerGamesClicked() }
        getGamesSortedAlphaButton.setOnClickListener { viewModel.onGetGamesSortedAlphabeticallyClicked() }
        getGamesBySpecificDeveloperButton.setOnClickListener { viewModel.onGetGamesBySpecificDeveloperClicked() }
        getGamesBySpecificReleaseYearButton.setOnClickListener { viewModel.onGetGamesBySpecificReleaseYearClicked() }
        getGamesInServerOrderButton.setOnClickListener { viewModel.onGetGamesInServerOrderClicked() }
        getGamesAndMediaInfo_1_Button.setOnClickListener { viewModel.onGetGamesAndMediaInfo_1_Clicked() }
        getGamesAndMediaInfo_2_Button.setOnClickListener { viewModel.onGetGamesAndMediaInfo_2_Clicked() }
        getGamesAndMediaInfo_3_Button.setOnClickListener { viewModel.onGetGamesAndMediaInfo_3_Clicked() }
        getGamesAndMediaInfo_4_Button.setOnClickListener { viewModel.onGetGamesAndMediaInfo_4_Clicked() }
    }

}