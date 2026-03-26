package org.example.project.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateShopListResponse(
    @SerialName("list_id")
    val listId: Long,
    val success: Boolean
)
