package com.jahid.blog_api.dto

import com.jahid.blog_api.model.Post
import com.jahid.blog_api.model.User

fun User.toDto(): UserDTO {
    return UserDTO(
        id = this.id,
        username = this.username,
        email = this.email,
        password = this.password
    )
}


fun Post.toDto(): PostDTO {
    return PostDTO(
        id = this.id,
        title = this.title,
        content = this.content,
        authorId = this.author.id,
        authorName = this.author.username
    )
}