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
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public BookRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Book> getBooks(Book book) {
        String sqlFull = "SELECT * FROM books ";

        if (!collectRequestString(book).isEmpty()) {
            sqlFull = sqlFull + " WHERE " + collectRequestString(book);
        }

        List<Book> books = jdbcTemplate.query(sqlFull, (ResultSet rs, int rowNum) -> {
            Book bookRs = new Book();
            bookRs.setId(rs.getInt("id"));
            bookRs.setAuthor(rs.getString("author"));
            bookRs.setTitle(rs.getString("title"));
            bookRs.setSize(rs.getInt("size"));
            return bookRs;

        });
        return new ArrayList<>(books);
    }

    public String collectRequestString(Book book) {
        String sql = "";
        if (book.getId() != null) {
            sql = sql + "id = " + book.getId() + " AND";
        }
        if (book.getAuthor() != null && !book.getAuthor().isEmpty()) {
            sql = sql + " author = \'" + book.getAuthor() + "\' AND";
        }
        if (book.getTitle() != null && !book.getTitle().isEmpty()) {
            sql = sql + " title = \'" + book.getTitle() + "\' AND";
        }
        if (book.getSize() != null) {
            sql = sql + " size = " + book.getSize();
        }

        if (!sql.isEmpty() && sql.substring(sql.length() - 3).equals("AND")) {
            sql = sql.substring(0, sql.length() - 3);
        }
        return sql;
    }


    @Override
    public void save(Book book) {
        String sql = "INSERT INTO books (author, title, size) " +
                "VALUES (:author, :title, :size)";
        jdbcTemplate.update(sql, new BeanPropertySqlParameterSource(book));
    }

    public void deleted(Book book) {
        String sql = "DELETE FROM BOOKS WHERE ";

        sql = sql + collectRequestString(book);
        System.out.println(sql);
        jdbcTemplate.update(sql, new BeanPropertySqlParameterSource(book));
    }


    // это все не нужно
    @Override
    public List<Book> getAllBooks() {
        return null;
    }

    @Override
    public List<Book> getSize(Integer size) {
        return null;
    }

    @Override
    public List<Book> getAuthor(String author) {
        return null;
    }

    @Override
    public List<Book> getTitle(String title) {
        return null;
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
