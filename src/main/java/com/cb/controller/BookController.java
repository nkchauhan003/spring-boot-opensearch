package com.cb.controller;

import com.cb.model.Book;
import com.cb.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    /**
     * Endpoint to save a single book.
     *
     * @param book The book to be saved.
     * @return The saved book.
     */
    @PostMapping
    public Book saveBook(@RequestBody Book book) {
        return bookService.saveBook(book);
    }

    /**
     * Endpoint to save multiple books in bulk.
     *
     * @param books The list of books to be saved.
     * @return The saved books.
     */
    @PostMapping("/bulk")
    public Iterable<Book> saveBooks(@RequestBody List<Book> books) {
        return bookService.saveBooks(books);
    }

    /**
     * Endpoint to search for books based on a query.
     *
     * @param query The search query.
     * @return A list of books matching the search query.
     * @throws IOException If an I/O error occurs.
     */
    @GetMapping("/search")
    public List<Book> searchBooks(@RequestParam String query) throws IOException {
        return bookService.searchBooks(query);
    }

    /**
     * Endpoint to get the count of books grouped by genre.
     *
     * @return A map with genre names as keys and the count of books as values.
     * @throws IOException If an I/O error occurs.
     */
    @GetMapping("/genre")
    public Map<String, Long> getGenreBuckets() throws IOException {
        return bookService.countBooksByGenre();
    }

    /**
     * Endpoint to search for books based on an optional genre and/or search query.
     * If neither is provided, returns all records in the index.
     *
     * @param genre The genre to filter by (optional).
     * @param query The search query (optional).
     * @return A list of books matching the search criteria.
     * @throws IOException If an I/O error occurs.
     */
    @GetMapping("/searchBooks")
    public List<Book> searchBooks(
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String query) throws IOException {
        return bookService.searchBooks(genre, query);
    }
}