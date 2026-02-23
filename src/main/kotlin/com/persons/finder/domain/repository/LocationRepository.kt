package com.persons.finder.domain.repository

import com.persons.finder.domain.model.Location

interface LocationRepository {
    fun findByReferenceId(referenceId: Long): Location?
    fun save(location: Location)
}
