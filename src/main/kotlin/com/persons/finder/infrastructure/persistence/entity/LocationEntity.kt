package com.persons.finder.infrastructure.persistence.entity

import javax.persistence.*
import org.locationtech.jts.geom.Point

@Entity
@Table(name = "locations")
data class LocationEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val personId: Long,
    val latitude: Double,
    val longitude: Double,

    @Column(name = "geom", columnDefinition = "geography(Point,4326)")
    var geom: Point? = null
)
