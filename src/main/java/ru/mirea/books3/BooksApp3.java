package ru.mirea.books3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RestController;

/**
 * Отключаем использование аннотации @RestController для определения бинов в этом примере,
 * так как здесь бины определяются через @Configuration
 */
@ComponentScan(excludeFilters = @ComponentScan.Filter(RestController.class))
@SpringBootApplication
public class BooksApp3 {

    public static void main(String[] args) {
        SpringApplication.run(BooksApp3.class, args);
    }
}
