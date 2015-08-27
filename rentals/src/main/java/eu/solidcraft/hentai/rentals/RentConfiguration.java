package eu.solidcraft.hentai.rentals;

import com.netflix.hystrix.contrib.javanica.aop.aspectj.HystrixCommandAspect;
import eu.solidcraft.hentai.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

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
    public RentReturner rentReturner(Environment environment) {
        return new RentReturner(rentRepository, rentPriceCalculator(environment));
    }

    @Bean
    public RentPriceCalculator rentPriceCalculator(Environment environment) {
        return new RentPriceCalculator(environment);
    }
}
