package eu.solidcraft.hentai.rentals;

import eu.solidcraft.hentai.films.Film;
import eu.solidcraft.hentai.films.FilmType;
import eu.solidcraft.hentai.infrastructure.TimeService;
import eu.solidcraft.hentai.users.User;
import eu.solidcraft.hentai.users.UserRepository;

import javax.transaction.Transactional;

import static org.springframework.util.Assert.*;

class RentCreator {
    private RentRepository rentRepository;
    private FilmCatalogueClient filmCatalogueClient;
    private UserRepository userRepository;
    private RentPriceCalculator rentPriceCalculator;

    public RentCreator(RentRepository rentRepository, FilmCatalogueClient filmCatalogueClient, UserRepository userRepository, RentPriceCalculator rentPriceCalculator) {
        this.rentRepository = rentRepository;
        this.filmCatalogueClient = filmCatalogueClient;
        this.userRepository = userRepository;
        this.rentPriceCalculator = rentPriceCalculator;
    }

    @Transactional
    Rent rent(Long filmId, Integer numberOfDays, String username) {
        validateParams(filmId, numberOfDays, username);
        FilmType filmType = getFilmType(filmId);
        Rent rent = new Rent(filmId, filmType, numberOfDays, username, TimeService.now(), rentPriceCalculator);
        rentRepository.save(rent);
        giveBonusPoints(username, filmType);
        return rent;
    }

    private void giveBonusPoints(String username, FilmType filmType) {
        User user = userRepository.findOne(username);
        user.addBonusPoints(filmType.getBonusPoints());
        userRepository.save(user);
    }

    private void validateParams(Long filmId, Integer numberOfDays, String username) {
        notNull(filmId);
        notNull(numberOfDays);
        isTrue(numberOfDays > 0);
        hasText(username);
    }

    private FilmType getFilmType(Long filmId) {
        Film film = filmCatalogueClient.findOne(filmId).getBody();
        verifyExists(filmId, film);
        return film.getFilmType();
    }

    private void verifyExists(Long filmId, Film film) {
        if (film == null) {
            throw new NoSuchFilmException(filmId);
        }
    }
}
