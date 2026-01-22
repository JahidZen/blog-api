package com.jahid.blog_api.service

import com.jahid.blog_api.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service


@Service
class CustomerDetailsService(private val userRepository: UserRepository) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username).orElseThrow { UsernameNotFoundException("Username $username not found") }


        return org.springframework.security.core.userdetails.User
            .withUsername(user.username)
            .password(user.password)
            .roles("USER")
            .build()
    }
}