package com.cb.service;

import com.cb.model.Book;
import com.cb.repository.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.opensearch.action.search.SearchRequest;
import org.opensearch.action.search.SearchResponse;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.index.query.QueryBuilders;
import org.opensearch.search.SearchHit;
import org.opensearch.search.aggregations.AggregationBuilders;
import org.opensearch.search.aggregations.bucket.terms.Terms;
import org.opensearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service class for managing books.
 */
@Service
public class BookService {
    private final RestHighLevelClient client;
    private final BookRepository bookRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${opensearch.index}")
    private String indexName;

    /**
     * Constructs a new BookService.
     *
     * @param client The RestHighLevelClient for interacting with OpenSearch.
     * @param bookRepository The repository for managing Book entities.
     */
    public BookService(RestHighLevelClient client, BookRepository bookRepository) {
        this.client = client;
        this.bookRepository = bookRepository;
    }

    /**
     * Saves a single book.
     *
     * @param book The book to be saved.
     * @return The saved book.
     */
    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    /**
     * Saves multiple books in bulk.
     *
     * @param books The list of books to be saved.
     * @return The saved books.
     */
    public List<Book> saveBooks(List<Book> books) {
        return StreamSupport
                .stream(bookRepository.saveAll(books).spliterator(), true)
                .collect(Collectors.toList());
    }

    /**
     * Searches for books based on a query.
     *
     * @param query The search query.
     * @return A list of books matching the search query.
     * @throws IOException If an I/O error occurs.
     */
    public List<Book> searchBooks(String query) throws IOException {
        SearchRequest searchRequest = new SearchRequest(indexName);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.queryStringQuery(query)
                .field("title")
                .field("description"));
        searchRequest.source(sourceBuilder);

        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        List<Book> books = new ArrayList<>();
        for (SearchHit hit : response.getHits().getHits()) {
            books.add(objectMapper.readValue(hit.getSourceAsString(), Book.class));
        }
        return books;
    }

    /**
     * Gets the count of books grouped by genre.
     *
     * @return A map with genre names as keys and the count of books as values.
     * @throws IOException If an I/O error occurs.
     */
    public Map<String, Long> countBooksByGenre() throws IOException {
        SearchRequest searchRequest = new SearchRequest(indexName);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.aggregation(AggregationBuilders.terms("genreAgg").field("genre"));
        searchRequest.source(sourceBuilder);

        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        Terms genreAgg = response.getAggregations().get("genreAgg");
        Map<String, Long> genreBuckets = new HashMap<>();
        for (Terms.Bucket bucket : genreAgg.getBuckets()) {
            genreBuckets.put(bucket.getKeyAsString(), bucket.getDocCount());
        }
        return genreBuckets;
    }

    /**
     * Searches for books based on an optional genre and/or search query.
     * If neither is provided, returns all records in the index.
     *
     * @param genre The genre to filter by (optional).
     * @param query The search query (optional).
     * @return A list of books matching the search criteria.
     * @throws IOException If an I/O error occurs.
     */
    public List<Book> searchBooks(String genre, String query) throws IOException {
        SearchRequest searchRequest = new SearchRequest(indexName);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        if (genre != null && !genre.isEmpty()) {
            sourceBuilder.query(QueryBuilders.termQuery("genre", genre));
        }

        if (query != null && !query.isEmpty()) {
            sourceBuilder.query(QueryBuilders.queryStringQuery(query)
                    .field("title")
                    .field("description"));
        }

        if ((genre == null || genre.isEmpty()) && (query == null || query.isEmpty())) {
            sourceBuilder.query(QueryBuilders.matchAllQuery());
        }

        searchRequest.source(sourceBuilder);

        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        List<Book> books = new ArrayList<>();
        for (SearchHit hit : response.getHits().getHits()) {
            books.add(objectMapper.readValue(hit.getSourceAsString(), Book.class));
        }
        return books;
    }
}