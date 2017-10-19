package com.codangcoding.popularmovies.internal.api

import com.fasterxml.jackson.annotation.JsonProperty

class ApiResponse<out Data>(
        val id: Int,
        val page: Int,
        @JsonProperty("total_results") val totalResults: Int,
        @JsonProperty("total_pages") val totalPages: Int,
        val results: Data
)