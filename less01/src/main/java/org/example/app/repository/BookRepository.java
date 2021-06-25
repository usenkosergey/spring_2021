package org.example.app.repository;

import org.apache.log4j.Logger;
import org.example.web.dto.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
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

    @Override
    public void save(Book book) {
        String sql = "INSERT INTO books (author, title, size) " +
                "VALUES (:author, :title, :size)";
        jdbcTemplate.update(sql, new BeanPropertySqlParameterSource(book));
    }

    @Override
    public List<Book> getAllBooks() {
        String sqlFull = "SELECT * FROM books ";

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

    @Override
    public List<Book> getSize(Integer size) {
        String sqlFull = "SELECT * FROM books WHERE size = :size";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("size", size);

        List<Book> books = jdbcQuery(sqlFull, parameterSource);
        return new ArrayList<>(books);
    }

    @Override
    public List<Book> getAuthor(String author) {
        String sqlFull = "SELECT * FROM books WHERE author = :author";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("author", author);

        List<Book> books = jdbcQuery(sqlFull, parameterSource);
        return new ArrayList<>(books);
    }

    @Override
    public List<Book> getTitle(String title) {
        String sqlFull = "SELECT * FROM books WHERE title = :title";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("title", title);

        List<Book> books = jdbcQuery(sqlFull, parameterSource);
        return new ArrayList<>(books);
    }

    public List<Book> jdbcQuery(String sqlFull, MapSqlParameterSource parameterSource) {

        return jdbcTemplate.query(sqlFull, parameterSource, (ResultSet rs, int rowNum) -> {
            Book bookRs = new Book();
            bookRs.setId(rs.getInt("id"));
            bookRs.setAuthor(rs.getString("author"));
            bookRs.setTitle(rs.getString("title"));
            bookRs.setSize(rs.getInt("size"));
            return bookRs;
        });

    }

    @Override
    public boolean removeItemById(Integer bookIdToRemove) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", bookIdToRemove);
        jdbcTemplate.update("DELETE FROM BOOKS WHERE id = :id", parameterSource);
        return false;
    }

    @Override
    public boolean removeItemBySize(Integer size) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("size", size);
        jdbcTemplate.update("DELETE FROM BOOKS WHERE size = :size", parameterSource);
        return false;
    }

    @Override
    public boolean removeItemByTitle(String title) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("title", title);
        jdbcTemplate.update("DELETE FROM BOOKS WHERE title = :title", parameterSource);
        return false;
    }

    @Override
    public boolean removeItemByAuthor(String author) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("author", author);
        jdbcTemplate.update("DELETE FROM BOOKS WHERE author = :author", parameterSource);
        return false;
    }
}
