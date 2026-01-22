package com.jahid.blog_api.controller  // Or controller package

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice  // Catches ALL controllers exceptions globally
class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException::class)
    fun handleRuntime(ex: RuntimeException): ResponseEntity<String> {
        // Ownership errors → 403 Forbidden
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.message)
    }

    // Bonus: Post/User not found → 404
    @ExceptionHandler(java.util.NoSuchElementException::class)  // orElseThrow default
    fun handleNotFound(ex: NoSuchElementException): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.message)
    }
}
