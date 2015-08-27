package eu.solidcraft.hentai.rentals;

import eu.solidcraft.hentai.rentals.price.FilmType;
import eu.solidcraft.hentai.rentals.price.PriceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;

import static org.springframework.util.Assert.notNull;

class RentPriceCalculator {
    private BigDecimal premiumPrice;
    private BigDecimal basicPrice;

    @Autowired
    public RentPriceCalculator(@Value("${renting.price.PREMIUM}")  BigDecimal premiumPrice, @Value("${renting.price.BASIC}") BigDecimal basicPrice) {
        this.premiumPrice = premiumPrice;
        this.basicPrice = basicPrice;
    }

    BigDecimal calculatePrice(FilmType filmType, Integer numberOfDays) {
        notNull(filmType); notNull(numberOfDays);
        BigDecimal priceForFirstDay = getPriceForFirstDay(filmType);
        return filmType.calculatePrice(priceForFirstDay, numberOfDays);
    }

    private BigDecimal getPriceForFirstDay(FilmType filmType) {
        BigDecimal priceForFirstDay = (filmType.getPriceType() == PriceType.PREMIUM) ? premiumPrice : basicPrice;
        validatePriceForFirstDay(priceForFirstDay, filmType);
        return priceForFirstDay;
    }

    private void validatePriceForFirstDay(BigDecimal priceForFirstDay, FilmType filmType) {
        if(priceForFirstDay == null) {
            throw new NoPriceForFilmTypeConfiguredException("No price configured for film type", filmType);
        }
    }
}

class NoPriceForFilmTypeConfiguredException extends RuntimeException {
    private FilmType filmType;

    public NoPriceForFilmTypeConfiguredException(String message, FilmType filmType) {
        super(message);
        this.filmType = filmType;
    }
}
