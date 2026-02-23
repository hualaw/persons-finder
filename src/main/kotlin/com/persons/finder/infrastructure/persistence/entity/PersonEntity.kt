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
    val hobbies: Array<String> = emptyArray(),
    var bio: String = ""
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PersonEntity) return false

        if (id != other.id) return false
        if (name != other.name) return false
        if (jobTitle != other.jobTitle) return false
        if (!hobbies.contentEquals(other.hobbies)) return false
        if (bio != other.bio) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + jobTitle.hashCode()
        result = 31 * result + hobbies.contentHashCode()
        result = 31 * result + bio.hashCode()
        return result
    }
}
