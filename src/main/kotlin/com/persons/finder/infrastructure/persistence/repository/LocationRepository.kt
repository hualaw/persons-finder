package com.persons.finder.infrastructure.persistence.repository

import com.persons.finder.infrastructure.persistence.entity.LocationEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface LocationRepository : JpaRepository<LocationEntity, Long> {
    fun findByPersonId(personId: Long): Optional<LocationEntity>

    @Query(value = "SELECT * FROM locations l WHERE ST_DWithin(l.geom, ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326), :radius)", nativeQuery = true)
    fun findNearby(@Param("latitude") latitude: Double, @Param("longitude") longitude: Double, @Param("radius") radius: Double): List<LocationEntity>
}
