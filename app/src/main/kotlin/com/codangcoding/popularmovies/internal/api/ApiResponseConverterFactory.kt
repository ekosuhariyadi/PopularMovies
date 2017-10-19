package com.codangcoding.popularmovies.internal.api

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.io.IOException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

class ApiResponseConverterFactory : Converter.Factory() {

    override fun responseBodyConverter(type: Type, annotations: Array<out Annotation>, retrofit: Retrofit): Converter<ResponseBody, *>? {
        val apiResponseType = ApiResponseParameterizedType(ApiResponse::class.java, type)
        val delegate: Converter<ResponseBody, ApiResponse<*>> = retrofit
                .nextResponseBodyConverter(this, apiResponseType, annotations)

        return ApiResponseConverter(delegate)
    }

    private class ApiResponseParameterizedType(
            private val rawType: Type,
            private val typeArgument: Type
    ) : ParameterizedType {

        override fun getRawType(): Type = rawType

        override fun getOwnerType(): Type? = null

        override fun getActualTypeArguments(): Array<Type> = arrayOf(typeArgument)
    }
}