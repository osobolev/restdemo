package ru.mirea.books;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping(path = "/api/books", produces = "application/json")
public class BookRest {

    private final List<Book> books = new CopyOnWriteArrayList<>();
    private final AtomicInteger idGenerator = new AtomicInteger();

    @GetMapping
    public List<Book> getAllBooks() {
        return books;
    }

    @GetMapping("/{id}")
    public Book getBook(@PathVariable("id") int id) {
        for (Book book : books) {
            if (book.getId() == id) {
                return book;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @PostMapping(consumes = "application/json")
    public Book addBook(@RequestBody BookDetails details) {
        if (details.getAuthor() == null || details.getTitle() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        int id = idGenerator.addAndGet(1);
        Book book = new Book(id, details.getAuthor(), details.getTitle());
        books.add(book);
        return book;
    }

    @PutMapping("/{id}")
    public Book updateBook(@PathVariable("id") int id, @RequestBody BookDetails details) {
        if (details.getAuthor() == null || details.getTitle() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        for (int i = 0; i < books.size(); i++) {
            Book book = books.get(i);
            if (book.getId() == id) {
                Book newBook = new Book(book.getId(), details.getAuthor(), details.getTitle());
                books.set(i, newBook);
                return newBook;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable("id") int id) {
        books.removeIf(book -> book.getId() == id);
    }
}
