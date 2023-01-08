package com.example.native202131.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserModel(
    @SerialName("id")
    val id: Int,
    @SerialName("repos_url")
    val reposUrl: String,
    @SerialName("updated_at")
    val updatedAt: String,
)
