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
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.*;
import java.util.List;

@Controller
@RequestMapping(value = "/books")
public class BookShelfController {

    private static final Logger logger = Logger.getLogger(BookShelfController.class);
    private final BookService bookService;

    @Autowired
    public BookShelfController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/shelf")
    public String books(Model model) {
        logger.info("got book shelf");
        updateModelForShelf(model);
        return "book_shelf";
    }

    @PostMapping("/save")
    public String saveBook(@Valid Book book,
                           BindingResult bindingResult,
                           Model model) {
        if (bindingResult.hasFieldErrors("author")) {
            updateModelForSave(model, book);
            return "book_shelf";
        } else if (bindingResult.hasFieldErrors("title")) {
            updateModelForSave(model, book);
            return "book_shelf";
        } else if (bindingResult.hasFieldErrors("size")) {
            updateModelForSave(model, book);
            return "book_shelf";
        } else {
            if (bookService.hasAllValuesEmpty(book)) {
                logger.info("book has all values empty :( ");
            } else {
                bookService.saveBook(book);
                logger.info("current repository size: " + bookService.getAllBooks().size());
            }
            return "redirect:/books/shelf";
        }
    }

    @PostMapping("/remove")
    public String removeBook(@Valid Book book,
                             BindingResult bindingResult,
                             Model model) {

        bindingResult.addError(new ObjectError("", "message"));

        //если есть id удаляем по id
        if (book.getId() != null) {
            if (bindingResult.hasFieldErrors("id")) {
                updateModelForRemove(model, book, bindingResult);
                return "book_shelf";
            } else {
                bookService.removeById(book.getId());
                return "redirect:/books/shelf";
            }
            //если есть автор удаляем по автору
        } else if (!book.getAuthor().equals("")) {
            if (bindingResult.hasFieldErrors("author")) {
                updateModelForRemove(model, book, bindingResult);
                return "book_shelf";
            } else {
                bookService.removeByAuthor(book.getAuthor());
                return "redirect:/books/shelf";
            }
            //если есть титульник удаляем по титульнику
        } else if (!book.getTitle().equals("")) {
            if (bindingResult.hasFieldErrors("title")) {
                updateModelForRemove(model, book, bindingResult);
                return "book_shelf";
            } else {
                bookService.removeByTitle(book.getTitle());
                return "redirect:/books/shelf";
            }
            //если есть размер удаляем по размеру
        } else if (book.getSize() != null) {
            if (bindingResult.hasFieldErrors("size")) {
                updateModelForRemove(model, book, bindingResult);
                return "book_shelf";
            } else {
                bookService.removeBySize(book.getSize());
                return "redirect:/books/shelf";
            }
            //иначе нет ничего возращаем на заглавную
        } else {
            updateModelForRemove(model, book, bindingResult);
            return "book_shelf";
        }

    }


    @PostMapping("/filter")
    public String filterBookByTitle(@Valid Book book,
                                    BindingResult bindingResult,
                                    Model model) {
        bindingResult.addError(new ObjectError("", "message"));

        //если есть автор фильтруем по автору
        if (!book.getAuthor().equals("")) {
                updateModelForFilter(model, book, bindingResult, bookService.getAllBooksByAuthor(book.getAuthor()));
                return "book_shelf";
        //если есть титульник фильтруем по титульнику
        } else if (!book.getTitle().equals("")) {
                updateModelForFilter(model, book, bindingResult, bookService.getAllBooksByTittle(book.getTitle()));
                return "book_shelf";
        //если есть размер фильтруем по размеру
        } else if (book.getSize() != null) {
                updateModelForFilter(model, book, bindingResult, bookService.getAllBooksBySize(book.getSize()));
                return "book_shelf";
        //иначе нет ничего возращаем на заглавную
        } else {
            return "redirect:/books/shelf";
        }
    }

    @PostMapping("uploadFile")
    public String uploadFile(@RequestParam("file") MultipartFile file) throws BookShelfUploadException, IOException {
        //Получаем имя и формат файла
        String name = file.getOriginalFilename();
        //Сохраняем файл как массив байтов
        byte[] bytes = file.getBytes();

        //проверяем если файл не пришёл
        if (bytes.length == 0) {
            logger.info("Attempt to upload an empty file");
            throw new BookShelfUploadException("Attempt to upload an empty file!");
        }

        //создаем директорию
        String rootPath = System.getProperty("catalina.home");
        File dir = new File(rootPath + File.separator + "external_uploads");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        //создаем и сохраняем сам файл
        //создаем путь
        File serverFile = new File(dir.getAbsolutePath() + File.separator + name);
        //открываем поток и передаем туда файл
        BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
        //передаем туда наши байты для записи
        stream.write(bytes);
        //закрываем поток
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

    private void updateModelForRemove(Model model,
                                      Book book,
                                      BindingResult bindingResult) {
        model.addAttribute("book", new Book());
        model.addAttribute("book2Remove", book);
        model.addAttribute("book2Filter", new Book());
        model.addAttribute("bookList", bookService.getAllBooks());

        model.addAttribute(BindingResult.MODEL_KEY_PREFIX + "book2Remove", bindingResult);
    }

    private void updateModelForFilter(Model model,
                                      Book book,
                                      BindingResult bindingResult,
                                      List<Book> bookFilter) {
        model.addAttribute("book", new Book());
        model.addAttribute("book2Remove", new Book());
        model.addAttribute("book2Filter", book);
        model.addAttribute("bookList", bookFilter);

        model.addAttribute(BindingResult.MODEL_KEY_PREFIX + "book2Filter", bindingResult);
    }

    private void updateModelForSave(Model model,
                                    Book book) {
        model.addAttribute("book", book);
        model.addAttribute("book2Remove", new Book());
        model.addAttribute("book2Filter", new Book());
        model.addAttribute("bookList", bookService.getAllBooks());
    }

    private void updateModelForShelf(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("book2Remove", new Book());
        model.addAttribute("book2Filter", new Book());
        model.addAttribute("bookList", bookService.getAllBooks());
    }

    @ExceptionHandler({BookShelfUploadException.class, IOException.class})
    public String handleEmptyFile(Model model, BookShelfUploadException exception) {
        logger.error(exception.getMessage());
        model.addAttribute("errorMessage", exception.getMessage());
        updateModelForShelf(model);
        return "book_shelf";
    }

}
