package com.andrewkingmarshall.videogamelibrary.database

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

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