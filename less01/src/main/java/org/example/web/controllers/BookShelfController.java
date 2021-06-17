package org.example.web.controllers;

import org.apache.log4j.Logger;
import org.example.app.services.BookService;
import org.example.web.dto.Book;
import org.example.web.dto.RemoveObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

@Controller
@RequestMapping(value = "/books")
public class BookShelfController {

    private Logger logger = Logger.getLogger(BookShelfController.class);
    private BookService bookService;

    @Autowired
    public BookShelfController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/shelf")
    public String books(Model model, Book book) {
        if (book.getSize() != null) {
            model.addAttribute("bookList", bookService.getSize(book.getSize()));
        } else if (book.getAuthor() != null && !book.getAuthor().isEmpty()) {
            model.addAttribute("bookList", bookService.getAuthor(book.getAuthor()));
        } else if (book.getTitle() != null && !book.getTitle().isEmpty()) {
            model.addAttribute("bookList", bookService.getTitle(book.getTitle()));
        } else {
            model.addAttribute("bookList", bookService.getAllBooks());
        }
        logger.info("got book shelf");
        model.addAttribute("book", new Book());
        model.addAttribute("removeObject", new RemoveObject());
        return "book_shelf";
    }

    @PostMapping(value = "/save")
    public String saveBook(@Valid Book book, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("book", book);
            model.addAttribute("bookList", bookService.getAllBooks());
            model.addAttribute("removeObject", new RemoveObject());
            return "book_shelf";
        } else {
            if (!book.getAuthor().isEmpty() ||
                    book.getSize() != null ||
                    !book.getTitle().isEmpty()) {
                bookService.saveBook(book);
            }
            logger.info("current repository size: " + bookService.getAllBooks().size());
            return "redirect:/books/shelf";
        }
    }

    @PostMapping("/remove")
    public String removeBook(@Valid RemoveObject removeObject, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("book", new Book());
            model.addAttribute("bookList", bookService.getAllBooks());
            model.addAttribute("removeObject", removeObject);
            return "book_shelf";
        } else {
            System.out.println("else");
            try {
                switch (removeObject.getRemoveTitle()) {
                    case "bookId":
                        bookService.removeBookById(Integer.parseInt(removeObject.getRemoveValue()));
                        break;
                    case "size":
                        bookService.removeItemBySize(Integer.parseInt(removeObject.getRemoveValue()));
                        break;
                    case "title":
                        bookService.removeItemByTitle(removeObject.getRemoveValue());
                        break;
                    case "author":
                        bookService.removeItemByAuthor(removeObject.getRemoveValue());
                        break;
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            return "redirect:/books/shelf";
        }
    }

    @PostMapping("/uploadFile")
    public String uplodeFile(@RequestParam("fileUp") MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            return "redirect:/books/shelf";
        }
        String fileName = file.getOriginalFilename();
        byte[] bytes = file.getBytes();

        String rootPath = System.getProperty("catalina.home");
        File dir = new File(rootPath + File.separator + "external_uploads");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File serverFile = new File(dir.getAbsolutePath() + File.separator + fileName);
        BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
        stream.write(bytes);
        stream.close();

        logger.info("new file saved at: " + serverFile.getAbsolutePath());
        return "redirect:/books/shelf";

    }
}
