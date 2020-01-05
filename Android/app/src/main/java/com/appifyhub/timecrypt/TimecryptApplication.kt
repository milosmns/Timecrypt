package com.appifyhub.timecrypt

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen

/**
 * The main Application class for this app.
 */
@Suppress("unused")
class TimecryptApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }

}