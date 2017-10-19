package com.codangcoding.popularmovies.internal.api

import android.view.View
import okhttp3.ResponseBody
import retrofit2.Converter

class ApiResponseConverter<Data>(private val delegate: Converter<ResponseBody, ApiResponse<Data>>)
    : Converter<ResponseBody, Data> {

    override fun convert(value: ResponseBody): Data {
        val convert = delegate.convert(value)

        return convert.results
    }
}