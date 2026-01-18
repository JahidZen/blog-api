package com.jahid.blog_api.repository

import com.jahid.blog_api.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long>