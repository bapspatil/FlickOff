package bapspatil.flickoff.di.builder

import bapspatil.flickoff.ui.about.AboutActivity
import bapspatil.flickoff.ui.about.AboutActivityModule
import bapspatil.flickoff.ui.details.DetailsActivity
import bapspatil.flickoff.ui.details.DetailsActivityModule
import bapspatil.flickoff.ui.main.MainActivity
import bapspatil.flickoff.ui.main.MainActivityModule
import bapspatil.flickoff.ui.splash.SplashActivity
import bapspatil.flickoff.ui.splash.SplashActivityModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

/*
** Created by Bapusaheb Patil {@link https://bapspatil.com}
*/

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = [SplashActivityModule::class])
    abstract fun bindSplashActivity(): SplashActivity

    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [DetailsActivityModule::class])
    abstract fun bindDetailsActivity(): DetailsActivity

    @ContributesAndroidInjector(modules = [AboutActivityModule::class])
    abstract fun bindAboutActivity(): AboutActivity

}