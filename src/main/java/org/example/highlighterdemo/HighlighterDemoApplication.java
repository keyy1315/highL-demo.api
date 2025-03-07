package org.example.highlighterdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class HighlighterDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(HighlighterDemoApplication.class, args);
    }

}
