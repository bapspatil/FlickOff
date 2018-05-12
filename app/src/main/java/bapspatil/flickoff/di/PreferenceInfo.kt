package bapspatil.flickoff.di

/*
 ** Created by Bapusaheb Patil {@link https://bapspatil.com}
 */

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

import javax.inject.Qualifier

@Qualifier
@Retention(RetentionPolicy.RUNTIME)
annotation class PreferenceInfo
