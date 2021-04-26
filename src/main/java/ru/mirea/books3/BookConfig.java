package ru.mirea.books3;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.mirea.books3.nonspring.BookService;

@Configuration
public class BookConfig {

    @Bean
    public BookService bookService() {
        return new BookService();
    }

    @Bean
    public BookRest bookRest() {
        return new BookRest(bookService());
    }
}
