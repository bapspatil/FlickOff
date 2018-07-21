package bapspatil.flickoff.ui.base

/*
 ** Created by Bapusaheb Patil {@link https://bapspatil.com}
 */

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableBoolean

import bapspatil.flickoff.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel<N>(schedulerProvider: SchedulerProvider) : ViewModel() {

    private val isLoading = ObservableBoolean(false)

    lateinit var  mSchedulerProvider: SchedulerProvider
    lateinit var compositeDisposable: CompositeDisposable

    init {
        mSchedulerProvider = schedulerProvider
        compositeDisposable = CompositeDisposable()
    }

    var navigator: N? = null

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    fun setIsLoading(isLoading: Boolean) {
        this.isLoading.set(isLoading)
    }
}
