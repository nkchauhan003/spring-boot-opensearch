package com.cb.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * Represents a book document in the OpenSearch index.
 */
@Document(indexName = "books")
@JsonIgnoreProperties(ignoreUnknown = true)
public record Book(
        /**
         * The unique identifier of the book.
         */
        @Id String id,

        /**
         * The title of the book.
         */
        @Field(type = FieldType.Text) String title,

        /**
         * The author of the book.
         */
        @Field(type = FieldType.Keyword) String author,

        /**
         * The genre of the book.
         */
        @Field(type = FieldType.Keyword) String genre,

        /**
         * The description of the book.
         */
        @Field(type = FieldType.Text) String description
) {
}