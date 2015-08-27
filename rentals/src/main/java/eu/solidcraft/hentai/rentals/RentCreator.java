package eu.solidcraft.hentai.rentals;

import eu.solidcraft.hentai.infrastructure.TimeService;
import eu.solidcraft.hentai.rentals.dtos.Film;
import eu.solidcraft.hentai.rentals.price.FilmType;
import eu.solidcraft.hentai.users.User;
import eu.solidcraft.hentai.users.UserRepository;

import javax.transaction.Transactional;
import java.util.Optional;

import static org.springframework.util.Assert.*;

class RentCreator {
    private RentRepository rentRepository;
    private FilmCatalogueClientWithHystrix filmCatalogueClient;
    private UserRepository userRepository;
    private RentPriceCalculator rentPriceCalculator;

    public RentCreator(RentRepository rentRepository, FilmCatalogueClientWithHystrix filmCatalogueClient, UserRepository userRepository, RentPriceCalculator rentPriceCalculator) {
        this.rentRepository = rentRepository;
        this.filmCatalogueClient = filmCatalogueClient;
        this.userRepository = userRepository;
        this.rentPriceCalculator = rentPriceCalculator;
    }

    @Transactional
    Rent rent(Long filmId, Integer numberOfDays, String username) {
        validateParams(filmId, numberOfDays, username);
        Optional<Film> optionalFilm  = filmCatalogueClient.getFilmById(filmId);
        Film film = optionalFilm.orElseThrow(() -> (new NoSuchFilmException(filmId))); //here is where we should propose another movie probably
        FilmType filmType = film.filmType;
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
}
