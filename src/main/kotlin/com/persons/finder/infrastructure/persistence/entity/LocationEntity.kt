package com.persons.finder.infrastructure.persistence.entity

import javax.persistence.*

@Entity
@Table(name = "locations")
data class LocationEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val personId: Long,
    val latitude: Double,
    val longitude: Double
)
