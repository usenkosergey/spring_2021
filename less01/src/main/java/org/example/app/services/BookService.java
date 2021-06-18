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

    public Book deleteBook(Book book) {
        if (!book.getAuthor().isEmpty() ||
                !book.getTitle().isEmpty() ||
                book.getId() != null ||
                book.getSize() != null) {
            bookRepo.deleted(book);
        }
        return new Book();
    }

    public List<Book> getBooks(Book book){
        return bookRepo.getBooks(book);
    }

}
