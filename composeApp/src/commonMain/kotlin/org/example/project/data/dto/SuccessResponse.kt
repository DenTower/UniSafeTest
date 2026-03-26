package org.example.project.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class SuccessResponse (
    val success: Boolean
)