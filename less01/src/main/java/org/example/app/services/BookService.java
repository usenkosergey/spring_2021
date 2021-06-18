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

    public void saveBook(Book book) {
        if (!book.getAuthor().isEmpty() && !book.getTitle().isEmpty()) {
            bookRepo.save(book);
        }
    }

    public void deleteBook(Book book) {
        if (!book.getAuthor().isEmpty() ||
                !book.getTitle().isEmpty() ||
                book.getId() != null ||
                book.getSize() != null) {
            bookRepo.deleted(book);
        }
    }
    //=========================


    public List<Book> getAllBooks() {
        return bookRepo.getAllBooks();
    }

    public List<Book> getSize(Integer size) {
        return bookRepo.getSize(size);
    }

    public List<Book> getAuthor(String author) {
        return bookRepo.getAuthor(author);
    }

    public List<Book> getTitle(String title) {
        return bookRepo.getTitle(title);
    }

}
