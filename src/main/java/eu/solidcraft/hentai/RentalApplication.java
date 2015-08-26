package eu.solidcraft.hentai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
@EnableSpringDataWebSupport
public class RentalApplication {

    public static void main(String[] arguments) {
        SpringApplication.run(RentalApplication.class, arguments);
    }
}
