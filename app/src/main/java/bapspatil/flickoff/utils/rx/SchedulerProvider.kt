package bapspatil.flickoff.utils.rx

/*
 ** Created by Bapusaheb Patil {@link https://bapspatil.com}
 */

import io.reactivex.Scheduler

interface SchedulerProvider {
    fun computation(): Scheduler

    fun io(): Scheduler

    fun ui(): Scheduler
}
