package com.jahid.blog_api.dto

data class UserDTO (
    val id: Long = 0,
    val username: String,
    val password: String?,
    val email: String,
)


data class PostDTO (
    val id: Long = 0,
    val title: String,
    val content: String,
    val authorId: Long = 0, // We only send the ID, not the whole user object!
    val authorName: String? = null
)