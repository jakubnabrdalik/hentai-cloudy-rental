package eu.solidcraft.hentai.films;

import eu.solidcraft.hentai.infrastructure.Profiles;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

import java.util.Optional;

@Profile({Profiles.DEV, Profiles.PROD})
public interface FilmRepository extends Repository<Film, Long> {

    Page<Film> findAll(Pageable pageable);

    Optional<Film> findOne(Long id);
}
