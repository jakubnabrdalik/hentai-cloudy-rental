package eu.solidcraft.hentai.rentals;

import com.netflix.hystrix.contrib.javanica.aop.aspectj.HystrixCommandAspect;
import eu.solidcraft.hentai.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
class RentConfiguration {
    @Autowired
    private RentRepository rentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FilmCatalogueClient filmCatalogueClient;

    @Bean
    public FilmCatalogueClientWithHystrix filmCatalogueClientWithHystrix() {
        return new FilmCatalogueClientWithHystrix(filmCatalogueClient);
    }

    @Bean
    public HystrixCommandAspect hystrixAspect() {
        return new HystrixCommandAspect();
    }

    @Bean
    public RentCreator rentCreator(FilmCatalogueClientWithHystrix filmCatalogueClientWithHystrix, RentPriceCalculator rentPriceCalculator) {
        return new RentCreator(rentRepository, filmCatalogueClientWithHystrix, userRepository, rentPriceCalculator);
    }

    @Bean
    public RentReturner rentReturner(RentPriceCalculator rentPriceCalculator) {
        return new RentReturner(rentRepository, rentPriceCalculator);
    }

    @Bean
    @RefreshScope
    public RentPriceCalculator rentPriceCalculator(@Value("${renting.price.PREMIUM}") BigDecimal premiumPrice, @Value("${renting.price.BASIC}") BigDecimal basicPrice) {
        return new RentPriceCalculator(premiumPrice, basicPrice);
    }
}
