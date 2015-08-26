package eu.solidcraft.hentai.films;

import org.springframework.data.rest.core.annotation.RestResource;

interface FilmCrudRepository extends FilmRepository {
    long count();

    @RestResource(exported = false)
    Film save(Film film);
}
