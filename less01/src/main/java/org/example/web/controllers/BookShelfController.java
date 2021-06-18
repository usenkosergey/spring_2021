package org.example.web.controllers;

import org.apache.log4j.Logger;
import org.example.app.services.BookService;
import org.example.web.dto.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

@Validated
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
    public String books(@Valid Book book, BindingResult bindingResult, @RequestParam(value = "action", defaultValue = "empty") String action, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("book", book);
            return "book_shelf";
        }

        if (action.equals("save")){
            bookService.saveBook(book);
        } else if (action.equals("deleted")){
            bookService.deleteBook(book);
        }

        logger.info("got book shelf");
        model.addAttribute("bookList", bookService.getBooks(book));
        model.addAttribute("book", new Book());
        return "book_shelf";
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
