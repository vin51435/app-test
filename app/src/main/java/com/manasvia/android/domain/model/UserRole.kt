package com.manasvia.android.domain.model

/**
 * User security role matching backend platform permissions.
 */
enum class UserRole {
    GUEST,
    USER,
    PREMIUM,
    ADMIN;

    companion object {
        fun fromString(role: String?): UserRole {
            return entries.firstOrNull { it.name.equals(role, ignoreCase = true) } ?: GUEST
        }
    }
}
