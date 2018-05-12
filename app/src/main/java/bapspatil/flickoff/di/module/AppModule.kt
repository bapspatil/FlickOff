package bapspatil.flickoff.di.module

import android.app.Application
import bapspatil.flickoff.BuildConfig
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/*
** Created by Bapusaheb Patil {@link https://bapspatil.com}
*/

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideApiKey(): String = BuildConfig.TMDB_API_TOKEN

    @Provides
    @Singleton
    fun provideContext(application: Application) = application

}