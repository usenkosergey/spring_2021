package org.example.app.repository;

import org.apache.log4j.Logger;
import org.example.web.dto.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
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
        String sql = "insert into books (author, title, size) " +
                "values (:author, :title, :size)";
        jdbcTemplate.update(sql, new BeanPropertySqlParameterSource(book));
    }

    public void deleted(Book book) {
        String sql = "DELETE FROM BOOKS WHERE ";
        if (book.getId() != null) {
            sql = sql + "id = " + book.getId() + " AND";
        }
        if (!book.getAuthor().isEmpty()) {
            sql = sql + " author = \'" + book.getAuthor() + "\' AND";
        }
        if (!book.getTitle().isEmpty()) {
            sql = sql + " title = \'" + book.getTitle() + "\' AND";
        }
        if (book.getSize() != null) {
            sql = sql + " size = " + book.getSize();
        }

        if (sql.substring(sql.length() - 3).equals("AND")) {
            sql = sql.substring(0, sql.length() - 3);
        }
        System.out.println(sql);
        jdbcTemplate.update(sql, new BeanPropertySqlParameterSource(book));
    }

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
        return false;
    }

    @Override
    public boolean removeItemBySize(Integer size) {
        return false;
    }

    @Override
    public boolean removeItemByTitle(String title) {
        return false;
    }

    @Override
    public boolean removeItemByAuthor(String author) {
        return false;
    }
}
