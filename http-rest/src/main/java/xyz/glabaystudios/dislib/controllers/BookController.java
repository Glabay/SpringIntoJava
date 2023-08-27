package xyz.glabaystudios.dislib.controllers;

import org.springframework.web.bind.annotation.*;
import xyz.glabaystudios.dislib.services.BookService;
import xyz.glabaystudios.dislib.services.HttpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import xyz.glabaystudios.network.dto.BookDTO;

import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
//  http://localhost:8080/api/v1/book
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

    @GetMapping("/all/no-shelf/{discordId}")
    public ResponseEntity<List<BookDTO>> getAllBooksNotOnAShelfForDiscordUser(@PathVariable("discordId") Long discordId) {
        var books = bookService.findAllBooksNotOnShelves(discordId);
        if (Objects.nonNull(books) && !books.isEmpty())
            return new ResponseEntity<>(books, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/all/{shelfId}/{discordId}")
    public ResponseEntity<List<BookDTO>> getAllBooksOnAShelfForDiscordUser(@PathVariable("discordId") Long discordId, @PathVariable("shelfId") Long shelfId) {
        var books = bookService.findAllForShelfIdForUser(shelfId, discordId);
        if (Objects.nonNull(books) && !books.isEmpty())
            return new ResponseEntity<>(books, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
