package com.example.native202131.network

import com.example.native202131.database.RepoEntity
import com.example.native202131.database.UserEntity
import java.util.*

fun UserModel.toEntity(): UserEntity {
    return UserEntity(
        id = 0,
        cachedAt = Date().time,
        login = login,
        loginId = id,
        name = name,
        reposUrl = reposUrl,
        updatedAt = updatedAt
    )
}

fun RepoModel.toEntity(ownerId: Int): RepoEntity {
    return RepoEntity(
        id = 0,
        ownerId = ownerId,
        repoId = id,
        name = name,
        updatedAt = updatedAt
    )
}

fun List<RepoModel>.toEntity(ownerId: Int): List<RepoEntity> {
    val list = mutableListOf<RepoEntity>()
    forEach { list.add(it.toEntity(ownerId)) }
    return list
}
