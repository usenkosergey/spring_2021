package org.example.app.repository;

import java.util.List;

public interface ProjectRepository<T> {
    List<T> getAllBooks();
    List<T> getSize(Integer size);
    List<T> getAuthor(String author);
    List<T> getTitle(String title);

    void save(T book);

    boolean removeItemById(Integer bookIdToRemove);

    boolean removeItemBySize(Integer size);

    boolean removeItemByTitle(String title);

    boolean removeItemByAuthor(String author);
}
