package com.example.restapi.controller;

import com.example.restapi.annotation.VerifyToken;
import com.example.restapi.model.Book;
import com.example.restapi.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

import java.util.Optional;

@RestController
@Validated
@RequestMapping("/api/books")
@Tag(name = "Books", description = "API for managing books")
@SecurityRequirement(name = "bearerAuth")
public class BookController {

    @Autowired
    private BookService bookService;

    @Operation(summary = "Get all books", description = "Returns a paginated list of all books")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all books", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class)) }),
    })
    @VerifyToken
    @GetMapping
    public ResponseEntity<Iterable<Book>> getAllBooks(
            // The maximum number of records to return in the response (pagination limit)
            @Parameter(description = "Limit number of records (minimum: 0, maximum: 50)", example = "10", required = false) @RequestParam(defaultValue = "10") @Min(0) @Max(50) int limit,
            @Parameter(description = "Skip records (minimum: 0, maximum: 1000)", example = "0", required = false) @RequestParam(defaultValue = "0") @Min(0) @Max(1000) int offset) {

        Pageable pageable = PageRequest.of(offset / limit, limit);
        return ResponseEntity.ok(bookService.getAllBooks(pageable));
    }

    @Operation(summary = "Get book by ID", description = "Returns a single book by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the book", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class)) }),
    })
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Optional<Book> book = bookService.getBookById(id);
        return book.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new book", description = "Creates a new book entry")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Book created", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class)) })
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    /**
     * Creates a new book entry in the system.
     *
     * @param book the book to be created
     * @return the created Book object
     */
    public Book createBook(@RequestBody Book book) {
        return bookService.createBook(book);
    }

    @Operation(summary = "Delete a book", description = "Deletes a book by its ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}