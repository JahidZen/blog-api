package com.jahid.blog_api.controller

import com.jahid.blog_api.dto.PostDTO
import com.jahid.blog_api.dto.UserDTO
import com.jahid.blog_api.service.BlogService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class BlogController(private val blogService: BlogService) {
    @PostMapping("/users")
    fun createUser(@RequestBody user: UserDTO): ResponseEntity<UserDTO> {
        val createdUser = blogService.createUser(user) // this createUser is called from BlogService
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser)
    }

    @PutMapping("/users/{id}")
    fun updateUser(@PathVariable id: Long, @RequestBody user: UserDTO): ResponseEntity<UserDTO> {
        val updatedUser = blogService.updateUser(
            id = id,
            newName = user.username,
            newEmail = user.email,
            newPassword = user.password,
        )
        return ResponseEntity.ok(updatedUser)
    }

    @PostMapping("/posts")
    fun createPost(@RequestBody post: PostDTO): ResponseEntity<PostDTO> {
        val createdPost = blogService.createPost(post)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost)
    }


    @PutMapping("/posts/{id}")
    fun updatePost(@PathVariable id: Long, @RequestBody post: PostDTO): ResponseEntity<PostDTO> {
        val updatedPost = blogService.updatePost(
            id = id,
            newTitle = post.title,
            newContent = post.content
        )
        return ResponseEntity.ok(updatedPost)
    }



    @DeleteMapping("users/{id}")
    fun deleteUser(@PathVariable id: Long): ResponseEntity<UserDTO> {
        val deletedUser = blogService.deleteId(
            id = id
        )
        return ResponseEntity.noContent().build()
    }


    @DeleteMapping("/posts/{id}")
    fun deletePost(@PathVariable id: Long): ResponseEntity<PostDTO> {
        val deletedPost = blogService.deletePost(
            postId = id
        )
        return ResponseEntity.noContent().build()
    }


    @GetMapping("/users")
    fun getAllUsers(): ResponseEntity<List<UserDTO>> {
        return ResponseEntity.ok(blogService.getAllUsers())
    }

    @GetMapping("/users/{id}")
    fun getUser(@PathVariable id: Long): ResponseEntity<UserDTO> {
        return ResponseEntity.ok(blogService.getUser(id))
    }


    @GetMapping("/posts")
    fun getAllPosts(): ResponseEntity<List<PostDTO>> {
        return ResponseEntity.ok(blogService.getAllPosts())
    }

    @GetMapping("/posts/{id}")
    fun getPost(@PathVariable id: Long): ResponseEntity<PostDTO> {
        return ResponseEntity.ok(blogService.getPost(id))
    }

}