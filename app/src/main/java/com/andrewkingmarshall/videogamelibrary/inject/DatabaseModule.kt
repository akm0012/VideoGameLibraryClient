package com.andrewkingmarshall.videogamelibrary.inject

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.realm.Realm

@Module
@InstallIn(ActivityComponent::class)
class DatabaseModule {

    @Provides
    fun provideRealm(): Realm {
        return Realm.getDefaultInstance()
    }

    @Provides
    fun provideRefreshEmitter(): Observable<Boolean> {

        // Todo: pretty sure this is wrong
        return Observable.create { emitter ->
            emitter.onNext(true)
            emitter.onComplete()
        }
    }

}
