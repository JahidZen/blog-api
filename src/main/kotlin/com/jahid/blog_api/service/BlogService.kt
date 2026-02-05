package com.jahid.blog_api.service

import com.jahid.blog_api.dto.PostDTO
import com.jahid.blog_api.dto.UserRegisterDTO
import com.jahid.blog_api.dto.UserResponseDTO
import com.jahid.blog_api.dto.toDto
import com.jahid.blog_api.dto.toResponse
import com.jahid.blog_api.model.Post
import com.jahid.blog_api.model.User
import com.jahid.blog_api.repository.PostRepository
import com.jahid.blog_api.repository.UserRepository
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class BlogService(
    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
    private val passwordEncoder: PasswordEncoder
) {
    fun createUser(userDto: UserRegisterDTO): UserResponseDTO {
        val userEntity = User(
            username = userDto.username,
            email = userDto.email,
            password = passwordEncoder.encode(userDto.password)
        )

        val savedUser = userRepository.save(userEntity)
        return savedUser.toResponse()
    }


    fun createPost(postDto: PostDTO): PostDTO {
        val author = userRepository.findById(postDto.authorId)
            .orElseThrow() { RuntimeException("User not found with id: ${postDto.authorId}") }
        val postEntity = Post(
            title = postDto.title,
            content = postDto.content,
            author = author
        )

        val savedPost = postRepository.save(postEntity)
        return savedPost.toDto()
    }


    // delete user id
    fun deleteId(id: Long) {
        val findUserId = userRepository.findById(id).orElseThrow() { RuntimeException("User not found with id: $id") }
        val currentIdName = getCurrentUsername()

        if (findUserId.username != currentIdName) {
            throw RuntimeException("You're not the owner, so you're not allowed to delete this user.")
        } else {
            userRepository.deleteById(id)
        }
    }


    // delete post
    fun deletePost(postId: Long) {
        val findPost =
            postRepository.findById(postId).orElseThrow() { RuntimeException("Post not found with id: $postId") }
        val currentUserName = getCurrentUsername()

        if (findPost.author.username != currentUserName) {
            throw RuntimeException("You're not allowed to delete this post")
        } else {
            postRepository.deleteById(postId)
        }
    }


    // editing a post
    fun updatePost(id: Long, newTitle: String?, newContent: String?): PostDTO {
        val findPost = postRepository.findById(id).orElseThrow() { RuntimeException("Post not found with id: $id") }
        val currentUserName = getCurrentUsername()
        when {
            findPost.author.username != currentUserName -> {
                throw RuntimeException("You're not the owner, so you're not allowed to update")
            }

            newTitle != null -> {
                findPost.title = newTitle
            }

            newContent != null -> {
                findPost.content = newContent
            }
        }

        val savedUpdate = postRepository.save(findPost)
        return savedUpdate.toDto()
    }


    // updating an user data
    fun updateUser(id: Long, newName: String?, newEmail: String?, newPassword: String?): UserResponseDTO {
        val findUser = userRepository.findById(id).orElseThrow() { RuntimeException("User not found with id: $id") }
        val currentUserName = getCurrentUsername()

        when {
            findUser.username != currentUserName ->
                throw RuntimeException("You're not the owner, so you're not allowed to update")

            newName != null -> {
                findUser.username = newName
            }

            newEmail != null -> {
                findUser.email = newEmail
            }

            newPassword != null -> {
                findUser.password = newPassword
            }
        }

        val updateUser = userRepository.save(findUser)
        return updateUser.toResponse()
    }


    // Implementing R of CRUD
    fun getUser(id: Long): UserResponseDTO {
        val findUser = userRepository.findById(id).orElseThrow() { RuntimeException("User not found with id: $id") }
        return findUser.toResponse()
    }

    fun getAllUsers(): List<UserResponseDTO> {
        return userRepository.findAll().map { it.toResponse() }
    }

    fun getPost(id: Long): PostDTO {
        val findPost = postRepository.findById(id).orElseThrow() { RuntimeException("Post not found with id: $id") }
        return findPost.toDto()
    }

    fun getAllPosts(): List<PostDTO> {
        return postRepository.findAll().map { it.toDto() }
    }


    // Spring checks who's currently logged in
    private fun getCurrentUsername(): String =
        SecurityContextHolder.getContext().authentication?.name ?: throw RuntimeException("User not found")

}