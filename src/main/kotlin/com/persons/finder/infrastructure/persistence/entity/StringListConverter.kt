package com.persons.finder.infrastructure.persistence.entity

import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter
class StringListConverter : AttributeConverter<List<String>, String> {

    override fun convertToDatabaseColumn(attribute: List<String>?): String? {
        return attribute?.joinToString(",")
    }

    override fun convertToEntityAttribute(dbData: String?): List<String>? {
        return dbData?.split(",")?.map { it.trim() }
    }
}
