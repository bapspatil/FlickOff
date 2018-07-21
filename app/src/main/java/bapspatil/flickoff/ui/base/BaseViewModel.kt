package bapspatil.flickoff.ui.base

/*
 ** Created by Bapusaheb Patil {@link https://bapspatil.com}
 */

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableBoolean
import bapspatil.flickoff.ui.about.AboutNavigator

import bapspatil.flickoff.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import java.lang.ref.WeakReference

abstract class BaseViewModel<N>(schedulerProvider: SchedulerProvider) : ViewModel() {

    val isLoading = ObservableBoolean(false)
    var mSchedulerProvider: SchedulerProvider = schedulerProvider
    var compositeDisposable: CompositeDisposable = CompositeDisposable()
    lateinit var mNavigator: WeakReference<N>

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    fun setIsLoading(isLoading: Boolean) {
        this.isLoading.set(isLoading)
    }

    fun getNavigator(): N? = mNavigator.get()

    fun setNavigator(navigator: N) {
        this.mNavigator = WeakReference(navigator)
    }
}
