package com.codangcoding.popularmovies.internal.di

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import com.codangcoding.popularmovies.PopMovieApp
import com.codangcoding.popularmovies.internal.api.ApiConstants
import dagger.android.AndroidInjection
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector


interface Injectable

object AppInjector {

    fun init(app: PopMovieApp) {
        DaggerAppComponent.builder()
                .application(app)
                .appModule(AppModule(ApiConstants.BASE_URL))
                .build()
                .inject(app)

        app.registerActivityLifecycleCallbacks(
                object : Application.ActivityLifecycleCallbacks {

                    override fun onActivityPaused(activity: Activity?) {
                    }

                    override fun onActivityResumed(activity: Activity?) {
                    }

                    override fun onActivityStarted(activity: Activity?) {
                    }

                    override fun onActivityDestroyed(activity: Activity?) {
                    }

                    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
                    }

                    override fun onActivityStopped(activity: Activity?) {
                    }

                    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                        handleActivity(activity)
                    }
                }
        )
    }

    private fun handleActivity(activity: Activity) {
        if (activity is Injectable || activity is HasSupportFragmentInjector) {
            AndroidInjection.inject(activity)
        }

        if (activity is FragmentActivity) {
            activity.supportFragmentManager
                    .registerFragmentLifecycleCallbacks(
                            object : FragmentManager.FragmentLifecycleCallbacks() {
                                override fun onFragmentCreated(fm: FragmentManager?, f: Fragment, savedInstanceState: Bundle?) {
                                    if (f is Injectable) {
                                        AndroidSupportInjection.inject(f)
                                    }
                                }
                            },
                            true)
        }
    }
}