package ru.mirea.books3.nonspring;

public class BookDetails {

    private final String author;
    private final String title;

    public BookDetails(String author, String title) {
        this.author = author;
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }
}
