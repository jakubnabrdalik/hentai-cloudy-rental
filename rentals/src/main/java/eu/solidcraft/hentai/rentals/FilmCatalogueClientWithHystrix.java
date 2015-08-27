package eu.solidcraft.hentai.rentals;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import eu.solidcraft.hentai.rentals.dtos.Film;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public class FilmCatalogueClientWithHystrix {
    private FilmCatalogueClient filmCatalogueClient;

    public FilmCatalogueClientWithHystrix(FilmCatalogueClient filmCatalogueClient) {
        this.filmCatalogueClient = filmCatalogueClient;
    }

    @HystrixCommand(
            fallbackMethod = "getFilmByIdFailure",
            //all options here: https://github.com/Netflix/Hystrix/wiki/Configuration
            commandProperties = {
                @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "100")
            },
            threadPoolProperties = {
                @HystrixProperty(name = "coreSize", value = "5"), // the maximum number of HystrixCommands that can execute concurrently. Default 10
                @HystrixProperty(name = "maxQueueSize", value = "101"), //If -1 then SynchronousQueue will be used, otherwise a positive value will be used with LinkedBlockingQueue.
                @HystrixProperty(name = "metrics.healthSnapshot.intervalInMilliseconds", value = "15") //time to wait, between allowing health snapshots to be taken that calculate success and error percentages and affect circuit breaker status.
            })
    public Optional<Film> getFilmById(final Long filmId) { //this could return Future or ObservableResult, to use it async, not waste resources, and make it explicit that it takes long
        ResponseEntity<Film> responseEntity = filmCatalogueClient.findOne(filmId);
        if(responseEntity.getStatusCode() != HttpStatus.OK) {
            return Optional.empty();
        }
        return Optional.of(responseEntity.getBody());
    }

    public Optional<Film> getFilmByIdFailure(final Long filmId) {
        return Optional.empty();
    }
}
