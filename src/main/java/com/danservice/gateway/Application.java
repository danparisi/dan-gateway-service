package com.danservice.gateway;

import com.danservice.techstarter.autoconfigure.DiscoveryClientConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication(exclude = DiscoveryClientConfiguration.class)
public class Application {

    public static void main(String[] args) {
        SpringApplication
                .run(Application.class, args);
    }

}
