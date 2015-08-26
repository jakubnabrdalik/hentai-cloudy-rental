package eu.solidcraft.hentai.films;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FilmCatalogueController {
    private FilmRepository filmRepository;

    @Autowired
    public FilmCatalogueController(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    @RequestMapping(value = "/films/{filmId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Film> findOne(@PathVariable Long filmId) {
        return filmRepository.findOne(filmId)
                .map(film -> new ResponseEntity<>(film, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/films", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity<PagedResources<Film>> findAll(Pageable pageable, PagedResourcesAssembler assembler) {
        Page<Film> films = filmRepository.findAll(pageable);
        return new ResponseEntity<>(assembler.toResource(films), HttpStatus.OK);
    }
}
