package com.jahid.blog_api.model

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.GeneratedValue
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity // This class talks to the database
@Table(name = "posts") // names the table in the database
data class Post(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val title: String = "",
    val content: String = "",

    // Many Posts belong to One User
    @ManyToOne(fetch = FetchType.LAZY) // Lazy means: Don't load the whole User unless I specifically ask for it
    @JoinColumn(name = "user_id") // This creates the foreign key column in the DB
    val author: User
)