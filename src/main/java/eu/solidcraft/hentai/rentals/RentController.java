package eu.solidcraft.hentai.rentals;

import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
public class RentController {
    private RentCreator rentCreator;
    private RentReturner rentReturner;

    @Autowired
    public RentController(RentCreator rentCreator, RentReturner rentReturner) {
        this.rentCreator = rentCreator;
        this.rentReturner = rentReturner;
    }

    @PreAuthorize("principal?.username == #username")
    @RequestMapping(value = "/rents", method = RequestMethod.POST)
    public Map<String, Long> rent(@RequestParam Long filmId, @RequestParam Integer numberOfDays, @RequestParam String username) {
        Rent rent = rentCreator.rent(filmId, numberOfDays, username);
        return ImmutableMap.of("rentId", rent.getId());
    }

    @RequestMapping(value = "/returns", method = RequestMethod.POST)
    public Map<String, BigDecimal> returnFilm(@RequestParam Long rentId) {
        Rent rent = rentReturner.returnFilm(rentId);
        return ImmutableMap.of("surcharge", rent.getLateReturnSurgcharge());
    }

    @ResponseStatus(value= HttpStatus.NOT_FOUND, reason="Film not found")
    @ExceptionHandler(NoSuchFilmException.class)
    public void statusForNoSuchFilm() {}

    @ResponseStatus(value= HttpStatus.NOT_FOUND, reason="Rent not found")
    @ExceptionHandler(NoSuchRentException.class)
    public void statusForNoSuchRent() {}
}
