package com.manasvia.android.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Standard uniform JSON response envelope received from the Manasvia Spring Boot backend.
 */
@Serializable
data class ApiResponseDto<T>(
    @SerialName("success") val success: Boolean = true,
    @SerialName("message") val message: String? = null,
    @SerialName("data") val data: T? = null,
    @SerialName("timestamp") val timestamp: String? = null
)
