package eu.solidcraft.hentai.rentals;

import eu.solidcraft.hentai.rentals.dtos.Film;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("FILMS")
interface FilmCatalogueClient {

    @RequestMapping(value = "/films/{filmId}", method = RequestMethod.GET)
    ResponseEntity<Film> findOne(@PathVariable("filmId") Long filmId);
}
