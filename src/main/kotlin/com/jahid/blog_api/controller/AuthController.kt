package com.jahid.blog_api.controller

import com.jahid.blog_api.service.JwtService
import com.jahid.blog_api.dto.LoginRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.naming.AuthenticationException


@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authenticationManager: AuthenticationManager, // Spring's own function; When user sends username and pass, it just validates if these are correct. Spring's login manager.
    private val jwtService: JwtService
) {
    @PostMapping("/login")
    // LoginRequest is data class from dto
    fun login(@RequestBody request: LoginRequest): ResponseEntity<Map<String, String>> {
        try {

            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(request.username, request.password)
            )
        } catch (e: AuthenticationException) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }

        val token = jwtService.generateToken(request.username)
        return ResponseEntity.ok(mapOf("token" to token))
    }
}