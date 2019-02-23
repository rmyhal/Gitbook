package com.rusmyhal.gitbook.model.entity

import com.google.gson.annotations.SerializedName


data class User(
    @SerializedName("login") val username: String,
    @SerializedName("id") val id: Long,
    @SerializedName("avatar_url") val avatarUrl: String?
)