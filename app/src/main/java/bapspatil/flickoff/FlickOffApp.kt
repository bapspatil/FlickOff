package bapspatil.flickoff

import android.app.Activity
import android.app.Application
import bapspatil.flickoff.di.component.DaggerAppComponent
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

/*
** Created by Bapusaheb Patil {@link https://bapspatil.com}
*/

class FlickOffApp : Application(), HasActivityInjector {

    @Inject lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector(): DispatchingAndroidInjector<Activity> = dispatchingActivityInjector

    override fun onCreate() {
        super.onCreate()

        DaggerAppComponent.builder()
                .application(this)
                .build()
                .inject(this)
    }
}