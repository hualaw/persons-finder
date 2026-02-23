package com.persons.finder.infrastructure.persistence.repository

import com.persons.finder.infrastructure.persistence.entity.LocationEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LocationRepository : JpaRepository<LocationEntity, Long>
