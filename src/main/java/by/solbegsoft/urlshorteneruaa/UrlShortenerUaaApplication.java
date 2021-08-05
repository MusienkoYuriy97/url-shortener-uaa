package by.solbegsoft.urlshorteneruaa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class UrlShortenerUaaApplication {

    public static void main(String[] args) {
        SpringApplication.run(UrlShortenerUaaApplication.class, args);
    }

}
