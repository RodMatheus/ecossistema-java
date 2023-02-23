package br.com.exemplo.comum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ComumApplication {

    public static void main(String[] args) {
        SpringApplication.run(ComumApplication.class, args);
    }

}