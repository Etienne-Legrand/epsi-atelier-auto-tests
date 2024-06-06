package com.epsi.crud;

import com.epsi.crud.model.Book;
import com.epsi.crud.service.BookService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class BookPersistenceTest {

    @Autowired
    private BookService bookService;

    private static final String BOOKS_FILE_PATH = "books.json";

    @BeforeEach
    void setUp() {
        // Clean up before each test
        File file = new File(BOOKS_FILE_PATH);
        if (file.exists()) {
            file.delete();
        }
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
    void testPersistenceBetweenRestarts() throws IOException {
        // Create and save 2 books
        Book book1 = createPersistentBook();
        Book book2 = createPersistentBook();
        bookService.createBook(book1);
        bookService.createBook(book2);

        // Verify the book is saved
        List<Book> booksBeforeRestart = bookService.getAllBooks();
        assertEquals(2, booksBeforeRestart.size());
        assertEquals("Persistent Book", booksBeforeRestart.get(0).getBookname());

        // Simulate application restart by reloading the service
        bookService = new BookService();

        // Verify the book is still present after restart
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
