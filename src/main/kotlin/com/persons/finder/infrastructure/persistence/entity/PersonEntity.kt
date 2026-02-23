package com.persons.finder.infrastructure.persistence.entity

import javax.persistence.*
import org.hibernate.annotations.Type

@Entity
@Table(name = "persons")
data class PersonEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val name: String,
    @Column(name = "job_title")
    val jobTitle: String,
    @Type(type = "com.vladmihalcea.hibernate.type.array.StringArrayType")
    @Column(
        name = "hobbies",
        columnDefinition = "text[]"
    )
    val hobbies: List<String> = emptyList(),
    var bio: String = ""
)
