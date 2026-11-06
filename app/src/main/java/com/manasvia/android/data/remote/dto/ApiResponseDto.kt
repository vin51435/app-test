package com.manasvia.android.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Standard uniform JSON response envelope received from the Manasvia Spring Boot backend.
 */
data class ApiResponseDto<T>(
    @SerializedName("success") val success: Boolean = true,
    @SerializedName("message") val message: String? = null,
    @SerializedName("data") val data: T? = null,
    @SerializedName("timestamp") val timestamp: String? = null
)
