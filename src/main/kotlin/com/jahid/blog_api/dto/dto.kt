package com.jahid.blog_api.dto

data class UserDTO (
    val id: Long,
    val username: String,
    val email: String,
)


data class PostDTO (
    val id: Long,
    val title: String,
    val content: String,
    val authorId: Long, // We only send the ID, not the whole user object!
    val authorName: String
)