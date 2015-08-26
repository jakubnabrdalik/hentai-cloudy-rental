package eu.solidcraft.hentai.rentals
import eu.solidcraft.hentai.films.Film
import eu.solidcraft.hentai.films.FilmCatalogueController
import groovy.transform.TypeChecked
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Primary
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.PathVariable

@TypeChecked
@Component
@Primary //because @Profile doesn't work on Feign interfaces
class FilmCatalogueClientStub implements FilmCatalogueClient {
    @Autowired
    private FilmCatalogueController filmCatalogueController

    @Override
    ResponseEntity<Film> findOne(@PathVariable("filmId") Long filmId) {
        return filmCatalogueController.findOne(filmId)
    }
}
