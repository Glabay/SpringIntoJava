package xyz.glabaystudios.dislib.controllers;

import xyz.glabaystudios.dislib.services.BookService;
import xyz.glabaystudios.dislib.services.HttpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.glabaystudios.network.dto.BookDTO;

import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/book")
public class BookController {
    private final BookService bookService;
    private final HttpService httpService;

    @PostMapping("/add/isbn/{isbn}")
    private ResponseEntity<BookDTO> handleAddingBookByISBN(@PathVariable Long isbn) {
        if (Objects.isNull(isbn))
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        if (isbn.toString().length() < 10 || isbn.toString().length() > 13)
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);

        BookDTO book = null;
        if (isbn.toString().length() == 10) book = bookService.getBookDtoForIsbn10(isbn);
        if (Objects.isNull(book) && isbn.toString().length() == 13) book = bookService.getBookDtoForIsbn13(isbn);
        if (Objects.isNull(book)) {
            var response = httpService.submitHttpGet("isbn", isbn.toString(), httpService.getHttpClient());
            if (Objects.nonNull(response)) {
                BookDTO newBook = bookService.parseBookDataObject(response);
                if (Objects.nonNull(newBook)) {
                    var newBookObject = bookService.createNewBookObject(newBook);
                    bookService.save(newBookObject);
                    return new ResponseEntity<>(newBook, HttpStatus.CREATED);
                } else
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

        }
        return new ResponseEntity<>(book, HttpStatus.OK);
    }
}
