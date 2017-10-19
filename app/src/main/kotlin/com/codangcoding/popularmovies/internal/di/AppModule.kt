package com.codangcoding.popularmovies.internal.di

import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.codangcoding.elmandroid.Program
import com.codangcoding.popularmovies.BuildConfig
import com.codangcoding.popularmovies.internal.api.ApiResponseConverterFactory
import com.codangcoding.popularmovies.internal.api.MovieDbService
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import dagger.Module
import dagger.Provides
import io.reactivex.android.schedulers.AndroidSchedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module(includes = arrayOf(ViewModelModule::class))
class AppModule(private val baseUrl: String) {

    companion object {
        private const val MOVIE_DB_API_KEY = BuildConfig.MOVIEDB_API_KEY
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(application: Application): SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(application)

    @Provides
    @Singleton
    fun provideObjectMapper(): ObjectMapper =
            ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .registerKotlinModule()

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient =
            OkHttpClient.Builder()
                    .addInterceptor { chain ->
                        val originRequest = chain.request()
                        val newUrl = originRequest.url().newBuilder()
                                .addQueryParameter("api_key", MOVIE_DB_API_KEY)
                                .build()
                        val newRequest = originRequest.newBuilder()
                                .url(newUrl)
                                .build()
                        chain.proceed(newRequest)
                    }
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build()

    @Provides
    @Singleton
    fun provideRetrofit(objectMapper: ObjectMapper, okHttpClient: OkHttpClient): Retrofit =
            Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(ApiResponseConverterFactory())
                    .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                    .client(okHttpClient)
                    .build()

    @Provides
    @Singleton
    fun provideMovieDbService(retrofit: Retrofit): MovieDbService =
            retrofit.create(MovieDbService::class.java)

    @Provides
    fun provideProgram(): Program =
            Program(AndroidSchedulers.mainThread())
}