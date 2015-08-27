package eu.solidcraft.hentai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

@SpringBootApplication
@EnableFeignClients
@EnableHystrixDashboard
@EnableHystrix
@EnableDiscoveryClient
public class RentalApplication {

    public static void main(String[] arguments) {
        SpringApplication.run(RentalApplication.class, arguments);
    }
}