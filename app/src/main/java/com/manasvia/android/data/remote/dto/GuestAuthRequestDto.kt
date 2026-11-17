package com.manasvia.android.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Payload sent to backend during anonymous guest session initialization.
 */
@Serializable
data class GuestAuthRequestDto(
    @SerialName("deviceFingerprint") val deviceFingerprint: String
)
