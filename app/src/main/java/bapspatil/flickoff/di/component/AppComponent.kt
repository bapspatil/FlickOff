package bapspatil.flickoff.di.component

import android.app.Application
import bapspatil.flickoff.FlickOffApp
import bapspatil.flickoff.di.builder.ActivityBuilder
import bapspatil.flickoff.di.module.AppModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

/*
** Created by Bapusaheb Patil {@link https://bapspatil.com}
*/

@Singleton
@Component(modules = [AndroidInjectionModule::class, AppModule::class, ActivityBuilder::class])
interface AppComponent {

    fun inject(flickOffApp: FlickOffApp): FlickOffApp

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}