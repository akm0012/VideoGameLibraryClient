package com.andrewkingmarshall.videogamelibrary.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.andrewkingmarshall.videogamelibrary.R
import com.andrewkingmarshall.videogamelibrary.inject.Injector
import com.andrewkingmarshall.videogamelibrary.network.dtos.VideoGameDto
import com.andrewkingmarshall.videogamelibrary.network.service.ApiService
import com.andrewkingmarshall.videogamelibrary.viewmodel.MainActivityViewModel
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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

        // Listen for button clicks with a 1 second delay so we don't spam the server
        RxView.clicks(button)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                makeApiCall()
            }
    }

    private fun makeApiCall() {

        viewModel.getAllVideoGames().observe(this, {gameList ->
            gameList.forEach {
                Log.i("GameTag", "Game: $it")
            }
        })
    }
}