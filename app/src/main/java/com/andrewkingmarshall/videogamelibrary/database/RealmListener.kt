package com.andrewkingmarshall.videogamelibrary.database

/**
 * Listens to when Async Realm Transactions are completed.
 */
interface RealmListener {
    fun transactionCompleted()
    fun errorOccurred(error: Throwable)
}