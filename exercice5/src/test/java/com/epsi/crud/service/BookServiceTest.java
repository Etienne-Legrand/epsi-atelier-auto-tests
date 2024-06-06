package com.epsi.crud.service;

import com.epsi.crud.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class BookServiceTest {

    private BookService bookService;

    @BeforeEach
    void setUp() {
        bookService = new BookService();
        bookService.getAllBooks().clear();  // Clear the list before each test
    }

    @Test
    void testCreateBook() {
        Book book = new Book();
        book.setBookname("Test Book");
        book.setAuthor("Test Author");
        book.setPrice(100);

        Book createdBook = bookService.createBook(book);

        assertNotNull(createdBook);
        assertEquals("Test Book", createdBook.getBookname());
        assertEquals("Test Author", createdBook.getAuthor());
        assertEquals(100, createdBook.getPrice());
    }

    @Test
    void testCreateBookWithExistingId() {
        Book book1 = new Book();
        book1.setBookname("Book 1");
        book1.setAuthor("Author 1");
        book1.setPrice(100);
        book1.setBookid(1L);

        Book book2 = new Book();
        book2.setBookname("Book 2");
        book2.setAuthor("Author 2");
        book2.setPrice(200);
        book2.setBookid(1L); // Same ID as book1

        bookService.createBook(book1);
        Book createdBook = bookService.createBook(book2);

        assertNull(createdBook);  // Should not create book with existing ID
    }

    @Test
    void testGetBookById() {
        Book book = new Book();
        book.setBookname("Test Book");
        book.setAuthor("Test Author");
        book.setPrice(100);

        Book createdBook = bookService.createBook(book);
        Optional<Book> foundBook = bookService.getBookById(createdBook.getBookid());

        assertTrue(foundBook.isPresent());
        assertEquals(createdBook.getBookname(), foundBook.get().getBookname());
    }

    @Test
    void testUpdateBook() {
        Book book = new Book();
        book.setBookname("Test Book");
        book.setAuthor("Test Author");
        book.setPrice(100);

        Book createdBook = bookService.createBook(book);

        Book updatedDetails = new Book();
        updatedDetails.setBookname("Updated Book");
        updatedDetails.setAuthor("Updated Author");
        updatedDetails.setPrice(150);

        Optional<Book> updatedBook = bookService.updateBook(createdBook.getBookid(), updatedDetails);

        assertTrue(updatedBook.isPresent());
        assertEquals("Updated Book", updatedBook.get().getBookname());
        assertEquals("Updated Author", updatedBook.get().getAuthor());
        assertEquals(150, updatedBook.get().getPrice());
    }

    @Test
    void testDeleteBook() {
        Book book = new Book();
        book.setBookname("Test Book");
        book.setAuthor("Test Author");
        book.setPrice(100);

        Book createdBook = bookService.createBook(book);
        bookService.deleteBook(createdBook.getBookid());

        Optional<Book> foundBook = bookService.getBookById(createdBook.getBookid());

        assertFalse(foundBook.isPresent());
    }
}
