package com.example.glassesgang;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * tests for the class BookList
 */
public class BookListTest {

    /**
     * creates a mock book list
     * @return bookList, a mock book list
     */
    private BookList mockBookList() {
        BookList bookList = new BookList();
        bookList.addBook(mockBook());
        return bookList;
    }

    /**
     * creates a mock book
     * @return a mock book
     */
    private Book mockBook() {
        return new Book("Lord of the Flies", "William Golding", "9780399501487", "testbid1", "testuser@gmail.com", "");
    }

    /**
     * tests if a book is added into the book list.
     */
    @Test
    void testAdd() {
        BookList bookList = mockBookList();

        assertEquals(1, bookList.getBooks().size());

        Book book = new Book("To Kill A Mockingbird", "Harper Lee", "9780446310789", "testbid2", "testuser@gmail.com", "");
        bookList.addBook(book);

        assertEquals(2, bookList.getBooks().size());
        assertTrue(bookList.getBooks().contains(book));
    }

    /**
     * test if a book is removed in the book list.
     */
    @Test
    void testRemove() {
        BookList bookList = mockBookList();

        assertEquals(1, bookList.getBooks().size());
        bookList.removeBook(mockBook());

        assertEquals(0, bookList.getBooks().size());
    }

    /**
     * test if clearing the book list empties out the list.
     */
    @Test
    void testClear() {
        BookList bookList = mockBookList();

        assertEquals(1, bookList.getBooks().size());

        bookList.clearBookList();

        assertEquals(0, bookList.getBooks().size());
    }


}
