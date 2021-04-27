package ru.mirea.books2;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/books", produces = "application/json")
public class BookRest {

    private final BookService service;

    public BookRest(BookService service) {
        this.service = service;
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return service.getAllBooks();
    }

    @GetMapping("/{id}")
    public Book getBook(@PathVariable("id") int id) {
        Optional<Book> found = service.getBook(id);
        return found.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping(consumes = "application/json")
    public Book addBook(@RequestBody BookDetails details) {
        return service.addBook(details);
    }

    @PutMapping(path = "/{id}", consumes = "application/json")
    public Book updateBook(@PathVariable("id") int id, @RequestBody BookDetails details) {
        return service.updateBook(id, details).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable("id") int id) {
        service.deleteBook(id);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleException(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
