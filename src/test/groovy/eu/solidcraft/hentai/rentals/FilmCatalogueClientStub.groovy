package eu.solidcraft.hentai.rentals
import eu.solidcraft.hentai.films.FilmCatalogueController
import eu.solidcraft.hentai.rentals.dtos.Film
import eu.solidcraft.hentai.rentals.price.FilmType
import groovy.transform.TypeChecked
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Primary
import org.springframework.http.HttpStatus
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
        eu.solidcraft.hentai.films.Film film = filmCatalogueController.findOne(filmId).getBody()
        Film filmDto = new Film()
        filmDto.description = film.description
        filmDto.id = film.id
        filmDto.title = film.title
        filmDto.filmType = FilmType.valueOf(film.filmType.toString())
        return new ResponseEntity<Film>(filmDto, HttpStatus.OK)
    }
}
