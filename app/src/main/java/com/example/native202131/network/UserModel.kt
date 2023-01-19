package com.example.native202131.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserModel(
    @SerialName("id")
    val id: Int,
    @SerialName("login")
    val login: String,
    @SerialName("name")
    val name: String,
    @SerialName("repos_url")
    val reposUrl: String,
    @SerialName("public_repos")
    val publicRepos: Int,
    @SerialName("updated_at")
    val updatedAt: String,
)
