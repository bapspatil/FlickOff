package bapspatil.flickoff.ui.base

/*
 ** Created by Bapusaheb Patil {@link https://bapspatil.com}
 */

import androidx.lifecycle.ViewModel
import androidx.databinding.ObservableBoolean

import bapspatil.flickoff.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import java.lang.ref.WeakReference

abstract class BaseViewModel<N>(schedulerProvider: SchedulerProvider) : ViewModel() {

    private val isLoading = ObservableBoolean(false)
    var mSchedulerProvider: SchedulerProvider = schedulerProvider
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()
    private lateinit var mNavigator: WeakReference<N>

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
