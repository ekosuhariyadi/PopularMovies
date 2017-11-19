package com.codangcoding.popularmovies.internal.di

import com.codangcoding.daggerandroidarch.base.registerDagger2InjectionCallback
import com.codangcoding.popularmovies.PopMovieApp
import com.codangcoding.popularmovies.internal.api.ApiConstants


object AppInjector {

    fun init(app: PopMovieApp) {
        DaggerAppComponent.builder()
                .application(app)
                .appModule(AppModule(ApiConstants.BASE_URL))
                .build()
                .inject(app)

        app.registerDagger2InjectionCallback()
    }
}