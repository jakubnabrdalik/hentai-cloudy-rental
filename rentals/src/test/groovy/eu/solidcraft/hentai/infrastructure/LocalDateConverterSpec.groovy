package eu.solidcraft.hentai.infrastructure

import groovy.transform.TypeChecked
import spock.lang.Specification

import java.time.LocalDate

@TypeChecked
class LocalDateConverterSpec extends Specification {
    LocalDateConverter converter = new LocalDateConverter()
    int year = 2015
    int month = 1
    int day = 2

    def "ConvertToDatabaseColumn"() {
        given:
            Date now = java.sql.Date.valueOf("$year-$month-$day")
        expect:
            converter.convertToEntityAttribute(now) == LocalDate.of(year, month, day)
    }

    def "convert to  LocalDate"() {
        given:
            LocalDate now = LocalDate.now()
        expect:
            converter.convertToDatabaseColumn(now).toLocalDate() == now
    }
}
