package org.example.app.services;

import org.example.app.repository.BookRepository;
import org.example.web.dto.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepo;

    @Autowired
    public BookService(BookRepository bookRepo) {
        this.bookRepo = bookRepo;
    }

    public Book saveBook(Book book) {
        if (!book.getAuthor().isEmpty() && !book.getTitle().isEmpty()) {
            bookRepo.save(book);
        }
        return new Book();
    }

    public boolean hasAllValuesEmpty(Book book) {
        return book.getAuthor().equals("") && book.getTitle().equals("") && book.getSize() == null;
    }

    public List<Book> getAllBooks() {
        return bookRepo.getAllBooks();
    }

    public void removeById(Integer id) {
        bookRepo.removeItemById(id);
    }

    public void removeBySize(Integer size) {
        bookRepo.removeItemBySize(size);
    }

    public void removeByAuthor(String author) {
        bookRepo.removeItemByAuthor(author);
    }


    public void removeByTitle(String title) {
        bookRepo.removeItemByTitle(title);
    }

    public List<Book> getAllBooksByAuthor(String author) {
        return bookRepo.getAuthor(author);
    }

    public List<Book> getAllBooksByTittle(String title) {
        return bookRepo.getTitle(title);
    }

    public List<Book> getAllBooksBySize(Integer size) {
        return bookRepo.getSize(size);
    }
}
