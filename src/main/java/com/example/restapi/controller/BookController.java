package com.example.restapi.controller;

import com.example.restapi.annotation.VerifyToken;
import com.example.restapi.model.Book;
import com.example.restapi.service.BookService;
import com.example.restapi.dto.PaginatedBookResponse;

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
import lombok.extern.slf4j.Slf4j;
import jakarta.validation.constraints.Max;

import java.util.List;
import java.util.Optional;

@RestController
@Validated
@RequestMapping("/api/books")
@Tag(name = "Books", description = "API for managing books")
@SecurityRequirement(name = "bearerAuth")
@SecurityRequirement(name = "cookieAuth")
@Slf4j

public class BookController {

    @Autowired
    private BookService bookService;

    public enum SubmissionStatus {
        DRAFT, SUBMITTED, PENDING_FOR_APPROVAL, COMPLETED
    }

    @Operation(summary = "Get all books", description = "Returns a list of books with pagination and filtering")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved books", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = PaginatedBookResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    // @VerifyToken
    @GetMapping
    public ResponseEntity<PaginatedBookResponse> getAllBooks(
            // The maximum number of records to return in the response (pagination limit)
            @Parameter(description = "Limit number of records (minimum: 0, maximum: 50)", example = "10", required = false) @RequestParam(defaultValue = "10") @Min(0) @Max(50) int limit,
            @Parameter(description = "Skip records (minimum: 0, maximum: 1000)", example = "0", required = false) @RequestParam(defaultValue = "0") @Min(0) @Max(1000) int offset,
            @Parameter(description = "Filter by book status. Accepts multiple values.", example = "[\"DRAFT\",\"SUBMITTED\"]", required = false, schema = @Schema(type = "array", allowableValues = {
                    "DRAFT", "SUBMITTED",
                    "PENDING_FOR_APPROVAL",
                    "COMPLETED" })) @RequestParam(name = "status", required = false) List<SubmissionStatus> status) {

        Pageable pageable = PageRequest.of(offset / limit, limit);
        log.info("Fetching books with limit: {}, offset: {}, status: {}", limit, offset, status);

        // Fetch paginated books and total count
        var page = bookService.getAllBooks(pageable);
        log.info("Fetched {} books", page);
        var response = new PaginatedBookResponse(
                page.getTotalElements(),
                offset,
                limit,

                page.getContent());
        return ResponseEntity.ok(response);
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