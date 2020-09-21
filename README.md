# Video Game Library Client
This is a simple app that plays around with some Rx concepts. I still need to add some unit tests for the ViewModel code. 

The backend code that supports this can be found here: https://github.com/akm0012/VideoGameLibrary

Here are some of the Rx functions used in this app.

```
fun onGetAllGamesClicked() {
        compositeDisposable.add(
            videoGameRepository.getAllVideoGames()
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { games ->
                    videoGameLiveData.value = games
                }
        )
    }

    fun onGetMultiPlayerGamesClicked() {
        compositeDisposable.add(
            videoGameRepository.getAllVideoGames()
                .filter { it.isMultiPlayer }
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { games ->
                    videoGameLiveData.value = games
                }
        )
    }

    fun onGetGamesSortedAlphabeticallyClicked() {
        compositeDisposable.add(
            videoGameRepository.getAllVideoGames()
                .toSortedList { g1, g2 -> g1.name.compareTo(g2.name) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { games ->
                    videoGameLiveData.value = games
                }
        )
    }

    fun onGetGamesBySpecificDeveloperClicked(developerName: String) {
        compositeDisposable.add(
            videoGameRepository.getAllVideoGames()
                .filter { it.developerStudio == developerName }
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { games ->
                    videoGameLiveData.value = games
                }
        )
    }

    fun onGetGamesBySpecificReleaseYearClicked(releaseYear: String) {
        compositeDisposable.add(
            videoGameRepository.getAllVideoGames()
                .filter {
                    val releaseDateTime: DateTime =
                        DateTimeFormat.forPattern("MMMM dd, yyyy").parseDateTime(it.dateReleased)
                    releaseDateTime.year == releaseYear.toInt()
                }
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { games ->
                    videoGameLiveData.value = games
                }
        )
    }

    fun onGetGamesInServerOrderClicked() {
        compositeDisposable.add(
            videoGameRepository.getAllVideoGameIds()
                .concatMapEager { videoGameRepository.getVideoGameDetails(it) }
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { games ->
                    videoGameLiveData.value = games
                }
        )
    }

    fun onGetGamesAndMediaInfoClicked() {
        compositeDisposable.add(videoGameRepository.getAllVideoGames()
            .flatMap { gameDto ->
                videoGameRepository.getVideoGameMediaInfo(gameDto.id)
                    .map {
                        gameDto.mediaInfo = it
                        gameDto
                    }
            }
            .toList()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { games ->
                videoGameLiveData.value = games
            }
        )
    }
```


![alt tag](https://github.com/akm0012/VideoGameLibraryClient/blob/master/ScreenShot.png)
