package eu.solidcraft.hentai.rentals
import base.TestData
import eu.solidcraft.hentai.rentals.dtos.Film
import groovy.transform.TypeChecked
import org.springframework.context.annotation.Primary
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.PathVariable

@TypeChecked
@Component
@Primary //because @Profile doesn't work on Feign interfaces
class FilmCatalogueClientStub implements FilmCatalogueClient {
    @Override
    ResponseEntity<Film> findOne(@PathVariable("filmId") Long filmId) {
        Film film = TestData.films.find {it.id == filmId}
        return new ResponseEntity<Film>(film, HttpStatus.OK)
    }
}
