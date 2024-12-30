package com.cb.repository;

import com.cb.model.Book;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.UUID;

/**
 * Repository interface for managing Book entities in OpenSearch.
 * Extends ElasticsearchRepository to provide CRUD operations and custom query methods.
 */
public interface BookRepository extends ElasticsearchRepository<Book, UUID> {

}