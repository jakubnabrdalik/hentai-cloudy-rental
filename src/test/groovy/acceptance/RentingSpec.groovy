package acceptance

import spock.lang.Specification

class RentingSpec extends Specification {

    def "customer can rent films"() {
        given: "there is a film in catalogue"

        when: "renting a film"

        then: "we get HTTP status 200 + rentId"

        when: "we search for the rent"

        then: "it's the rent for the film we wanted"

        when: "we return the film"

        and: "we search for the rent again"

        then: "the film is returned"
    }

    def "renting a new release should give 2 points per rental"() {
    }

    def "renting non-new release should give one point per rental"() {
    }
}
