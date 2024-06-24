package com.epsi.crud;

import com.epsi.crud.model.Book;
import com.epsi.crud.service.BookService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class BookPersistenceTest {

    @Autowired
    private BookService bookService;

    private static final String BOOKS_FILE_PATH = "booksPersistence.json";

    @TestConfiguration
    static class BookServiceTestConfig {
        @Bean
        @Primary
        public BookService bookService() {
            return new BookService(BOOKS_FILE_PATH);
        }
    }

    @BeforeEach
    void setUp() {
        // Clean up before each test
        File file = new File(BOOKS_FILE_PATH);
        if (file.exists()) {
            file.delete();
        }
        bookService = new BookService(BOOKS_FILE_PATH);
    }

    @AfterEach
    void tearDown() {
        // Clean up after each test
        File file = new File(BOOKS_FILE_PATH);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    void testPersistenceBetweenRestarts() {
        // Create and save 2 books
        Book book1 = createPersistentBook();
        Book book2 = createPersistentBook();
        bookService.createBook(book1);
        bookService.createBook(book2);

        // Verify the books are saved
        List<Book> booksBeforeRestart = bookService.getAllBooks();
        assertEquals(2, booksBeforeRestart.size());
        assertEquals("Persistent Book", booksBeforeRestart.get(0).getBookname());

        // Simulate application restart by reloading the service
        bookService = new BookService(BOOKS_FILE_PATH);

        // Verify the books are still present after restart
        List<Book> booksAfterRestart = bookService.getAllBooks();
        assertEquals(2, booksAfterRestart.size());
        assertEquals("Persistent Book", booksAfterRestart.get(0).getBookname());
        assertEquals("Persistent Author", booksAfterRestart.get(0).getAuthor());
        assertEquals(100, booksAfterRestart.get(0).getPrice());
    }

    private Book createPersistentBook() {
        Book book = new Book();
        book.setBookname("Persistent Book");
        book.setAuthor("Persistent Author");
        book.setPrice(100);
        return book;
    }
}
