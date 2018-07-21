package bapspatil.flickoff.ui.about

import bapspatil.flickoff.utils.rx.SchedulerProvider
import dagger.Module
import dagger.Provides

/*
** Created by Bapusaheb Patil {@link https://bapspatil.com}
*/

@Module
class AboutActivityModule {

    @Provides
    fun provideAboutViewModel(schedulerProvider: SchedulerProvider): AboutViewModel = AboutViewModel(schedulerProvider)
}