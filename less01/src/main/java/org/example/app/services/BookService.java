package org.example.app.services;

import org.example.app.repository.ProjectRepository;
import org.example.web.dto.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final ProjectRepository<Book> bookRepo;

    @Autowired
    public BookService(ProjectRepository<Book> bookRepo) {
        this.bookRepo = bookRepo;
    }

    public List<Book> getAllBooks() {
        return bookRepo.getAllBooks();
    }

    public List<Book> getSize(Integer size){
        return bookRepo.getSize(size);
    }

    public List<Book> getAuthor(String author){
        return bookRepo.getAuthor(author);
    }

    public List<Book> getTitle(String title){
        return bookRepo.getTitle(title);
    }

    public void saveBook(Book book) {
        bookRepo.save(book);
    }

    public boolean removeBookById(Integer bookIdToRemove) {
        return bookRepo.removeItemById(bookIdToRemove);
    }

    public boolean removeItemBySize(Integer size) {
        return bookRepo.removeItemBySize(size);
    }

    public boolean removeItemByTitle(String title){
        return bookRepo.removeItemByTitle(title);
    }

    public boolean removeItemByAuthor(String author){
        return bookRepo.removeItemByAuthor(author);
    }
}
