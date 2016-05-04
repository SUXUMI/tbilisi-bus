package com.tbilisi.bus

import android.content.Context
import android.util.Log
import com.tbilisi.bus.data.BusStop
import io.realm.Realm
import io.realm.RealmConfiguration

class DatabaseManager(val context: Context) {
    fun initialize() {
        // set default Realm configuration to use Realm.getDefaultInstance
        Realm.setDefaultConfiguration(RealmConfiguration.Builder(context).build())

        if(!isInitialized()) {
            Log.i("DatabaseManager", "Populating database")
            populate()
        }
    }

    fun populate() {
        val realm = Realm.getDefaultInstance()
        val jsonStream = context.assets.open("db.json")

        realm.beginTransaction()
        try {
            realm.createAllFromJson(BusStop::class.java, jsonStream)
        } catch(exception: Exception) {
            exception.printStackTrace()
            realm.cancelTransaction()
        } finally {
            realm.commitTransaction()
            jsonStream.close()
            Log.i("DatabaseManager", "Database populated")
        }
    }

    fun isInitialized(): Boolean {
        return Realm.getDefaultInstance().where(BusStop::class.java).count() > 0
    }
}