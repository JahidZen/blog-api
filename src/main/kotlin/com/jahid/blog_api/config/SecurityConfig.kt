package com.jahid.blog_api.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.config.Customizer
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.provisioning.InMemoryUserDetailsManager


@Configuration
@EnableWebSecurity
class SecurityConfig {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf { it.disable() }
            .authorizeHttpRequests { //// Disabling CSRF cause we don't need work with cookies in REST api. Cookies are in web browsers, in that case it should be enabled.
                    auth ->
                auth.requestMatchers(HttpMethod.POST, "/api/users").permitAll() // permits everyone to create account
                auth.requestMatchers(HttpMethod.GET, "/api/posts").permitAll() // permits everyone to read posts
                auth.anyRequest().authenticated() // except the above the permits, everything else requires login
            }.httpBasic(Customizer.withDefaults()) // httpBasic tells Spring to verify users in basic way with name and password.

        return http.build()
    }


    @Bean
    fun userDetailsService(): UserDetailsService {
        val user = User.builder()
        .username("jahid")
        .password("{noop}password")
            .roles("USER")
        .build()

        return InMemoryUserDetailsManager(user)
    }
}