package com.jahid.blog_api.controller

import com.jahid.blog_api.model.User
import com.jahid.blog_api.repository.UserRepository
import com.jahid.blog_api.service.JwtService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") // Uses application-test.properties (H2 DB)
@Transactional // <--- THE FIX: Keeps the session open and rolls back changes after test
class BlogControllerIntegrationTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var jwtService: JwtService

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    @BeforeEach
    fun setup() {
        // Good practice: Ensure DB is empty before starting to avoid ID collisions
        userRepository.deleteAll()
    }

    @Test
    fun `should create post if user is authorized`() {
        // 1. Setup: Create user using the Encoder (like the teacher's example)
        // We use .save() and capture the result in 'savedUser' to ensure we have the generated ID
        val user = User(
            id = 0,
            username = "Jahir",
            email = "jahi@yahoo.com",
            password = passwordEncoder.encode("passwordd")
        )
        val savedUser = userRepository.save(user)

        // 2. Generate Token
        val token = jwtService.generateToken(savedUser.username)

        // 3. JSON Payload (Use savedUser.id to be safe)
        val postJson = """
            {
                "title": "Integration test",
                "content": "We're testing the full API work flow",
                "authorId": ${savedUser.id}
            }
        """.trimIndent()

        // 4. Perform Request
        mockMvc.perform(
            post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer $token")
                .content(postJson)
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.title").value("Integration test"))
        // Optional: Verify author name if your DTO returns it
        // .andExpect(jsonPath("$.authorName").value("Jahir"))
    }
}