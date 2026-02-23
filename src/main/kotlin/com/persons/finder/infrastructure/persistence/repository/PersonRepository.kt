package com.persons.finder.infrastructure.persistence.repository

import com.persons.finder.infrastructure.persistence.entity.PersonEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PersonRepository : JpaRepository<PersonEntity, Long>
