package xyz.glabaystudios.dislib.controllers;

import xyz.glabaystudios.dislib.services.BookShelfService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.glabaystudios.network.dto.BookShelfDTO;

import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bookshelf")
public class BookshelfController {
    private final BookShelfService bookShelfService;

    @PostMapping("/create")
    private ResponseEntity<String> createNewBookshelf(@RequestBody BookShelfDTO dto) {
        if (Objects.isNull(dto))
            return new ResponseEntity<>("No shelf provided", HttpStatus.NO_CONTENT);
        var newShelf = bookShelfService.createNewBookShelf(dto.getShelfName(), dto.getShelfDescription(), dto.getOwnerDiscordId());
        bookShelfService.saveShelf(newShelf);
        return new ResponseEntity<>("Successfully added new shelf", HttpStatus.OK);
    }
    @PostMapping("/update")
    private ResponseEntity<String> updateBookshelf(@RequestBody BookShelfDTO dto) {
        if (Objects.isNull(dto))
            return new ResponseEntity<>("No shelf provided", HttpStatus.NO_CONTENT);
        var cachedShelf = bookShelfService.getBookshelfForId(dto.getShelfId());
        var updatedShelf = bookShelfService.updateBookShelf(cachedShelf, dto);
        bookShelfService.saveShelf(updatedShelf);
        return new ResponseEntity<>("Successfully update shelf", HttpStatus.OK);
    }

    @GetMapping("/fetch/shelves/{discordUserId}")
    private ResponseEntity<List<BookShelfDTO>> getBookshelvesForDiscordUser(@PathVariable Long discordUserId) {
        var shelves = bookShelfService.getBookShelvesForUser(discordUserId);
        if (Objects.isNull(shelves))
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        else
            return new ResponseEntity<>(shelves, HttpStatus.OK);
    }
}
