package eu.solidcraft.hentai.films;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

import java.util.Optional;

interface FilmRepository extends Repository<Film, Long> {

    Page<Film> findAll(Pageable pageable);

    Optional<Film> findOne(Long id);
}
