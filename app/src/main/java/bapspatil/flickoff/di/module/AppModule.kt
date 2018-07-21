package bapspatil.flickoff.di.module

import android.app.Application
import android.content.Context
import bapspatil.flickoff.BuildConfig
import bapspatil.flickoff.utils.rx.AppSchedulerProvider
import bapspatil.flickoff.utils.rx.SchedulerProvider
import com.google.gson.Gson
import com.google.gson.GsonBuilder
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
    fun provideContext(application: Application): Context = application

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()

    @Provides
    fun provideSchedulerProvider(): SchedulerProvider = AppSchedulerProvider()
}