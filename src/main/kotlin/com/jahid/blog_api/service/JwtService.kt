package com.jahid.blog_api.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.Date

@Service
class JwtService {
    private val secretKey = Keys.hmacShaKeyFor(System.getenv("JWT_SECRET").toByteArray())

    fun generateToken(username: String): String {
        return Jwts.builder()
            .setSubject(username) // Who is this token for?
            .setIssuedAt(Date()) // When was it made?
            .setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // expires in 24 hours
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact()
    }




    // Extract the Username
    fun extractUsername(token: String): String {
        return extractClaim(token, Claims::getSubject)
    }


    // Generic helper to extract any data
    fun <T> extractClaim(token: String, claimsResolver: (Claims) -> T): T {
        val claims = extractAllClaims(token)
        return claimsResolver(claims)
    }


    // Parse the Token (The heavy lifting)
    private fun extractAllClaims(token: String): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .body
    }


    // validate the token
    fun isTokenValid(token: String, userDetails: UserDetails): Boolean {
        val username = extractUsername(token)
        return (username == userDetails.username && !isTokenExpired(token))
    }


    private fun isTokenExpired(token: String): Boolean {
        return extractClaim(token, Claims::getExpiration).before(Date())
    }
}