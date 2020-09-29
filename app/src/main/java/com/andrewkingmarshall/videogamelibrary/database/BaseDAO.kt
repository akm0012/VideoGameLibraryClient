package com.andrewkingmarshall.videogamelibrary.database

import android.app.Application
import io.realm.DynamicRealm
import io.realm.Realm
import io.realm.RealmConfiguration
import timber.log.Timber

const val REALM_NAME = "videogamelibrary.realm"

const val REALM_SCHEMA_VERSION = 0L

fun initRealm(context: Application) {
    Realm.init(context)
    Realm.setDefaultConfiguration(getRealmConfiguration())
}

fun getRealmConfiguration(): RealmConfiguration {
    val realmBuilder = RealmConfiguration.Builder().apply {
        name(REALM_NAME)
        schemaVersion(REALM_SCHEMA_VERSION)
        deleteRealmIfMigrationNeeded()
    }

    return realmBuilder.build()
}

fun clearRealm() {
    Timber.d("Clearing Realm...")
    val defaultConfiguration = Realm.getDefaultConfiguration()
    defaultConfiguration?.let {
        DynamicRealm.getInstance(it).run {
            executeTransaction { deleteAll() }
            close()
        }
    }
    Timber.d("Realm cleared.")
}

