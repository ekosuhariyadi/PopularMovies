package com.codangcoding.popularmovies.internal.di

import com.codangcoding.popularmovies.detail.DetailActivity
import com.codangcoding.popularmovies.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun contributeDetailActivity(): DetailActivity
}