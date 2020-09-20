package com.andrewkingmarshall.videogamelibrary.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.andrewkingmarshall.videogamelibrary.R
import com.andrewkingmarshall.videogamelibrary.inject.Injector
import com.andrewkingmarshall.videogamelibrary.network.service.ApiService
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Injector.obtain().inject(this)

        setContentView(R.layout.activity_main)

        init()
    }

    private fun init() {
        RxView.clicks(button)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                makeApiCall()
            }
    }

    private fun makeApiCall() {
        apiService.getVideoGameIds()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    Log.i("games", it.toString())
                },
                { throwable ->
                    Log.w("games", throwable.localizedMessage, throwable)
                },
                {
                    Log.d("games", "All done!")
                }
            )
    }
}