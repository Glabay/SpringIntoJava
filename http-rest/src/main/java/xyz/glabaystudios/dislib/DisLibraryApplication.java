package xyz.glabaystudios.dislib;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("xyz.glabaystudios")
public class DisLibraryApplication {

    public static void main(String[] args) {
        SpringApplication.run(DisLibraryApplication.class, args);
    }

}
