package com.manasvia.android.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Payload sent to backend during anonymous guest session initialization.
 */
data class GuestAuthRequestDto(
    @SerializedName("deviceFingerprint") val deviceFingerprint: String
)
