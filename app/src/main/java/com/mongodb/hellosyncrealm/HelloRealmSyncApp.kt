package com.mongodb.hellosyncrealm

import android.app.Application
import io.realm.Realm
import io.realm.mongodb.App
import io.realm.mongodb.AppConfiguration

class HelloRealmSyncApp : Application() {

    val realmSync by lazy {
        App(AppConfiguration.Builder(BuildConfig.RealmAppId).build())
    }

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }

}