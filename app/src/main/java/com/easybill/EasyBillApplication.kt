package com.easybill

import android.app.Application
import timber.log.Timber

/**
 * Entry-point of the application.
*/
class EasyBillApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // setup timber for logging
        Timber.plant(Timber.DebugTree())
    }
}
