package com.example.restapi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import org.springframework.context.annotation.Primary;

import jakarta.persistence.Column;

@Entity
@Table(name = "books")
public class Book {

    /**
     * Primary key for Book entity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank
    @Size(min = 0, max = 20)
    private String title;

    @Column(nullable = false)
    @Size(min = 0, max = 30)
    private String author;

    @Column(unique = true, nullable = false)
    private String isbn;

    // Default constructor
    public Book() {
    }

    // Constructor with fields
    public Book(String title, String author, String isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    // public void setId(Long id) {
    // this.id = id;
    // }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

}