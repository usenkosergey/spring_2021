package org.example.web.controllers;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.example.app.exceptions.BookShelfUploadException;
import org.example.app.services.BookService;
import org.example.web.dto.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Path;
import javax.validation.Valid;
import java.io.*;
import java.nio.file.Files;

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
    public String uplodeFile(@RequestParam("fileUp") MultipartFile file) throws BookShelfUploadException, IOException {
        if (file.isEmpty()) {
            throw new BookShelfUploadException("file is absent");
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

    @GetMapping("/downloadFile")
    public ResponseEntity<byte[]> downloadFile() throws IOException, BookShelfUploadException {
        //Открываем поток на директорию с файлом
        try (InputStream io = getClass()
                .getClassLoader()
                .getResourceAsStream("/images/book_icon.png")) {

            HttpHeaders httpHeaders = new HttpHeaders();
            //указываем хедеры
            httpHeaders.setContentDisposition(ContentDisposition
                    //это приложение
                    .builder("attachment")
                    //имя файла при загрузке
                    .filename("book_icon.png")
                    .build());
            //если файла нет брось NullPointerException
            assert io != null;
            //иначе формируем ответ
            return ResponseEntity.ok()
                    //указываем контент тайп
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    //ставим наши хэдеры
                    .headers(httpHeaders)
                    //в боди формируем сам файл
                    .body(IOUtils.toByteArray(io));
            //отловили NullPointer упоковали его в нашу ошибку
        } catch (NullPointerException ex) {
            throw new BookShelfUploadException("file is absent");
        }
    }

    @ExceptionHandler({BookShelfUploadException.class, IOException.class})
    public String handleEmptyFile(Model model, BookShelfUploadException exception) {
        logger.error(exception.getMessage());
        model.addAttribute("errorMessage", exception.getMessage());
        Book book = new Book();
        BindingResult errors = new BeanPropertyBindingResult(book, "book");
        books(book, errors, "action", model);
        return "book_shelf";
    }

}
