package ru.mirea.books3.nonspring;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class BookService {

    private final List<Book> books = new CopyOnWriteArrayList<>();
    private final AtomicInteger idGenerator = new AtomicInteger();

    public List<Book> getAllBooks() {
        return books;
    }

    private static void checkDetails(BookDetails details) {
        if (details.getAuthor() == null)
            throw new IllegalArgumentException("No author");
        if (details.getTitle() == null)
            throw new IllegalArgumentException("No title");
    }

    public Optional<Book> getBook(int id) {
        for (Book book : books) {
            if (book.getId() == id) {
                return Optional.of(book);
            }
        }
        return Optional.empty();
    }

    public Book addBook(BookDetails details) {
        checkDetails(details);
        int id = idGenerator.addAndGet(1);
        Book book = new Book(id, details.getAuthor(), details.getTitle());
        books.add(book);
        return book;
    }

    public Optional<Book> updateBook(int id, BookDetails details) {
        checkDetails(details);
        for (int i = 0; i < books.size(); i++) {
            Book book = books.get(i);
            if (book.getId() == id) {
                Book newBook = new Book(book.getId(), details.getAuthor(), details.getTitle());
                books.set(i, newBook);
                return Optional.of(newBook);
            }
        }
        return Optional.empty();
    }

    public boolean deleteBook(int id) {
        return books.removeIf(book -> book.getId() == id);
    }
}
