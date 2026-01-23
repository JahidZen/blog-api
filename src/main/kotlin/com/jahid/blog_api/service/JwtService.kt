package com.jahid.blog_api.service

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.cglib.core.Signature
import java.util.Date

class JwtService {
    private val secretKey = Keys.hmacShaKeyFor("mySuperSecretKeyThatIsVeryLongAndSecure123".toByteArray())

    fun generateToken(username: String): String {
        return Jwts.builder()
            .setSubject(username) // Who is this token for?
            .setIssuedAt(Date()) // When was it made?
            .setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // expires in 24 hours
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact()
    }
}