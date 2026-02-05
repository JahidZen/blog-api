package com.jahid.blog_api.service

import com.jahid.blog_api.dto.PostDTO
import com.jahid.blog_api.model.Post
import com.jahid.blog_api.model.User
import com.jahid.blog_api.repository.PostRepository
import com.jahid.blog_api.repository.UserRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.mockito.Mockito.any
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.Optional
import kotlin.test.Test


@ExtendWith(MockitoExtension::class)
class BlogServiceTest {
    @Mock // Create a fake UserRepository
    lateinit var userRepository: UserRepository


    @Mock // Create a fake PostRepository
    lateinit var postRepository: PostRepository

    @Mock
    lateinit var passwordEncoder: PasswordEncoder


    @InjectMocks // Inject the fake repos into the real Service
    lateinit var blogService: BlogService



    @Test
    fun `should create post successfully`() {
        // Let's prepare the data
        val userId = 1L
        val postDTO = PostDTO(
            title = "test title",
            content = "test content",
            authorId = userId,
            authorName = "jahid",
        )

        val mockUser = User(
            id = userId,
            username = "jahid",
            email = "miracle@gmail.com",
            password = "hash"
        )

        val mockSavedPost = Post(
            id = 101,
            title = "Test title",
            content = "Test content",
            author = mockUser
        )


        // Let's teach the mock (WHEN)
        `when`(userRepository.findById(userId)).thenReturn(Optional.of(mockUser))

        `when`(postRepository.save(any(Post::class.java))).thenReturn(mockSavedPost)

        // JUnit processing
        val result = blogService.createPost(postDTO)

        assertEquals(101, result.id)
        assertEquals("Test title", result.title)
        assertEquals("Test content", result.content)
        assertEquals(userId, result.authorId)
    }





    // Let's check if the user don't exist
    @Test
    fun `should fail to create post if user not found`() {
        val userId = 99L
        val postDTO = PostDTO(
            0,
            title = "test title",
            content = "test content",
            authorId = userId,
            authorName = "ghost"
        )

        `when`(userRepository.findById(userId)).thenReturn(Optional.empty())

        // Processing. Should expect an exception
        val exception = assertThrows(RuntimeException::class.java) {
            blogService.createPost(postDTO)
        }


        assertEquals("User not found with id: 99", exception.message)
    }

}