package ru.kirea.carontrajectory

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.kirea.carontrajectory.di.Modules

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                Modules.carTrajectoryWindow
            )
        }
    }
}