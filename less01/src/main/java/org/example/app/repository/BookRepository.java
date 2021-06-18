package org.example.app.repository;

import org.apache.log4j.Logger;
import org.example.web.dto.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BookRepository implements ProjectRepository<Book> {

    private final Logger logger = Logger.getLogger(BookRepository.class);
    private final List<Book> repo = new ArrayList<>();

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public BookRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Book> getAllBooks() {
        List<Book> books = jdbcTemplate.query("SELECT * FROM books", (ResultSet rs, int rowNum) -> {
            Book book = new Book();
            book.setId(rs.getInt("id"));
            book.setAuthor(rs.getString("author"));
            book.setTitle(rs.getString("title"));
            book.setSize(rs.getInt("size"));
            return book;
        });
        return new ArrayList<>(books);
    }

    @Override
    public void save(Book book) {
        System.out.println("2-" + book.toString());
        String sql = "insert into books (author, title, size) " +
                "values (:author, :title, :size)";
        jdbcTemplate.update(sql, new BeanPropertySqlParameterSource(book));
    }


//    String sql = "insert into Person (first_Name, Last_Name, Address) " +
//            "values (:firstName, :lastName, :address)";
//        jdbcTemplate.update(sql, new BeanPropertySqlParameterSource(person));
//

    @Override
    public List<Book> getSize(Integer size) {
        List<Book> books = new ArrayList<>();
        for (Book b : repo) {
            if (b.getSize() != null && b.getSize().equals(size)) {
                books.add(b);
            }
        }
        return books;
    }

    @Override
    public List<Book> getAuthor(String author) {
        List<Book> books = new ArrayList<>();
        for (Book b : repo) {
            if (b.getAuthor() != null && b.getAuthor().equals(author)) {
                books.add(b);
            }
        }
        return books;
    }

    @Override
    public List<Book> getTitle(String title) {
        List<Book> books = new ArrayList<>();
        for (Book b : repo) {
            if (b.getTitle() != null && b.getTitle().equals(title)) {
                books.add(b);
            }
        }
        return books;
    }

    @Override
    public boolean removeItemById(Integer bookIdToRemove) {
        for (Book book : getAllBooks()) {
            if (book.getId().equals(bookIdToRemove)) {
                logger.info("remove book completed: " + book);
                return repo.remove(book);
            }
        }
        return false;
    }

    @Override
    public boolean removeItemBySize(Integer size) {
        int startRepo = repo.size();
        for (Book book : getAllBooks()) {
            if (book.getSize().equals(size)) {
                repo.remove(book);
            }
        }
        return startRepo - repo.size() != 0;
    }

    @Override
    public boolean removeItemByTitle(String title) {
        int startRepo = repo.size();
        for (Book book : getAllBooks()) {
            if (book.getTitle().equals(title)) {
                repo.remove(book);
            }
        }
        return startRepo - repo.size() != 0;
    }

    @Override
    public boolean removeItemByAuthor(String author) {
        int startRepo = repo.size();
        for (Book book : getAllBooks()) {
            if (book.getAuthor().equals(author)) {
                repo.remove(book);
            }
        }
        return startRepo - repo.size() != 0;
    }
}
