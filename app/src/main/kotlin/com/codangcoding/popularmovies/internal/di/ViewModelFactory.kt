package com.codangcoding.popularmovies.internal.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class ViewModelFactory @Inject constructor(private val creators: MutableMap<Class<out ViewModel>, Provider<ViewModel>>)
    : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val creator = creators.getOrElse(modelClass) {
            creators.entries.firstOrNull { modelClass.isAssignableFrom(it.key) }?.value
        } ?: throw IllegalArgumentException("unknown model class " + modelClass)

        try {
            return creator.get() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    }
}