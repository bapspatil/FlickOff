package bapspatil.flickoff.ui.main

import android.arch.lifecycle.ViewModelProvider
import bapspatil.flickoff.ViewModelProviderFactory
import bapspatil.flickoff.utils.rx.SchedulerProvider
import dagger.Module
import dagger.Provides

/*
** Created by Bapusaheb Patil {@link https://bapspatil.com}
*/

@Module
class MainActivityModule {

    @Provides
    fun mainViewModel(schedulerProvider: SchedulerProvider): MainViewModel = MainViewModel(schedulerProvider)

    @Provides
    fun provideMovieAdapter(): MovieRecyclerViewAdapter = MovieRecyclerViewAdapter(ArrayList())

    @Provides
    fun provideMainViewModel(mainViewModel: MainViewModel): ViewModelProvider.Factory = ViewModelProviderFactory(mainViewModel)
}