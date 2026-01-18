package com.jahid.blog_api.repository

import com.jahid.blog_api.model.Post
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository : JpaRepository<Post, Long>