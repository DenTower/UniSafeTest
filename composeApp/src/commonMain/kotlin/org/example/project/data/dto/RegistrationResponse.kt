package org.example.project.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class RegistrationResponse (
    val key: String,
    val success: Boolean
)