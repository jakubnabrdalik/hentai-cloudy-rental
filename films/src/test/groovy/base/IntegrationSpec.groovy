package base

import eu.solidcraft.hentai.FilmApplication
import eu.solidcraft.hentai.films.Film
import eu.solidcraft.hentai.films.FilmCrudRepository
import eu.solidcraft.hentai.infrastructure.Profiles
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Specification

@Rollback
@Transactional
@SpringApplicationConfiguration(classes = FilmApplication)
@ActiveProfiles(Profiles.TEST)
@Ignore
class IntegrationSpec extends Specification {
    @Autowired
    protected FilmCrudRepository filmRepository

    @Shared protected ArrayList<Film> persistedFilms = []

    void setup() {
        moviesArePresent()
    }

    protected moviesArePresent() {
        if(filmRepository.count() == 0) {
            persistedFilms = TestData.films.collect {filmRepository.save(it)}
        }
    }
}
