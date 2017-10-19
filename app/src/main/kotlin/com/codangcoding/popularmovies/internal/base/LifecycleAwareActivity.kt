package com.codangcoding.popularmovies.internal.base

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LifecycleRegistry
import android.support.v7.app.AppCompatActivity
import com.codangcoding.popularmovies.internal.di.Injectable

open class LifecycleAwareActivity : AppCompatActivity(), LifecycleOwner, Injectable {

    private val lifecycleRegistry = LifecycleRegistry(this)

    override fun getLifecycle(): Lifecycle =
            lifecycleRegistry
}