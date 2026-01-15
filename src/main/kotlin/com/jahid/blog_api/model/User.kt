package com.jahid.blog_api.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Table
import jakarta.persistence.Id
import jakarta.persistence.Column
import jakarta.persistence.FetchType
import jakarta.persistence.OneToMany
import jakarta.persistence.CascadeType

@Entity
@Table(name = "users")
data class User (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Automatically and incrementally generating id numbers
    var id: Long = 0,

    @Column(unique = true) // it says that the data in the username column must be unique compared to other usernames
    var username: String = "",
    val email: String = "",

    // One user has many posts
    // mappedBy tells Spring: "Go look at the 'author' field in Post to figure this out"
    @OneToMany(mappedBy = "author", cascade = [CascadeType.ALL], orphanRemoval = true)
    val posts: List<Post> = emptyList()
)