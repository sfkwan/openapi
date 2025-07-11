package com.example.restapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
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

    @Version
    private Long version;
}