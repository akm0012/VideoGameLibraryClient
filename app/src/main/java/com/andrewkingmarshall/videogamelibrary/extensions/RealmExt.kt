package com.andrewkingmarshall.videogamelibrary.extensions

import com.andrewkingmarshall.videogamelibrary.BuildConfig
import com.andrewkingmarshall.videogamelibrary.database.RealmListener
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import io.realm.RealmModel
import io.realm.RealmObject
import io.realm.RealmQuery
import timber.log.Timber

typealias Query<T> = RealmQuery<T>.() -> Unit

/**
 * Saves a Realm object Synchronously.
 */
fun RealmObject.save() {
    val realmOuter = Realm.getDefaultInstance()

    try {
        realmOuter.executeTransaction {
            it.insertOrUpdate(this)
        }

        Timber.d("${this.javaClass.simpleName} was saved successfully!")
    } catch (exception: Exception) {
        Timber.e(exception, "There was an error while saving ${this.javaClass.simpleName}")
        if (BuildConfig.DEBUG) {
            throw exception
        }
    } finally {
        realmOuter.close()
    }
}

fun List<RealmObject>.save() {
    val realmOuter = Realm.getDefaultInstance()

    try {
        realmOuter.executeTransaction {
            it.insertOrUpdate(this)
        }

        Timber.d("${this.javaClass.simpleName} was saved successfully!")
    } catch (exception: Exception) {
        Timber.e(exception, "There was an error while saving ${this.javaClass.simpleName}")
        if (BuildConfig.DEBUG) {
            throw exception
        }
    } finally {
        realmOuter.close()
    }
}

/**
 * Saves a Realm Object Asynchronously.
 */
fun RealmObject.saveAsync(realmListener: RealmListener?) {
    val realmOuter = Realm.getDefaultInstance()

    realmOuter.executeTransactionAsync({
        it.insertOrUpdate(this)
    }, {
        // On Success
        Timber.d("${this.javaClass.simpleName} was saved successfully!")
        realmListener?.transactionCompleted()

    }, {
        // On Error
        Timber.e(it, "There was an error while saving ${this.javaClass.simpleName}")
        realmListener?.errorOccurred(it)
    })

    realmOuter.close()
}

// Todo: test this
fun <T : RealmObject> saveRx(realmObject: T): Observable<T> {
    return Observable.fromCallable {
        realmObject.save()
        realmObject
    }.subscribeOn(Schedulers.io())
}

// Todo: test this
fun <T : RealmObject> saveRx(realmObject: List<T>): Observable<List<T>> {
    return Observable.fromCallable {
        realmObject.save()
        realmObject
    }.subscribeOn(Schedulers.io())
}

/**
 * Query to the database with RealmQuery instance as argument. Return first result, or null.
 */
fun <T : RealmModel> T.queryFirst(query: Query<T>): T? {
    Realm.getDefaultInstance().use { realm ->
        val result: T? = realm.where(this.javaClass).withQuery(query).findFirst()
        return if (result != null && RealmObject.isValid(result)) realm.copyFromRealm(result) else null
    }
}

/**
 * Return first result, or null.
 */
fun <T : RealmModel> T.queryFirst(): T? {
    Realm.getDefaultInstance().use { realm ->
        val result: T? = realm.where(this.javaClass).findFirst()
        return if (result != null && RealmObject.isValid(result)) realm.copyFromRealm(result) else null
    }
}

private fun <T> T.withQuery(block: (T) -> Unit): T {
    block(this); return this
}

fun <T> RealmQuery<T>.equalToValue(fieldName: String, value: String): RealmQuery<T> = equalTo(
    fieldName,
    value
)

