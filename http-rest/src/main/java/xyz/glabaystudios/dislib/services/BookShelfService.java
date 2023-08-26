package xyz.glabaystudios.dislib.services;

import xyz.glabaystudios.dislib.data.model.BookShelf;
import xyz.glabaystudios.dislib.data.repos.BookShelfRepository;
import xyz.glabaystudios.dislib.util.DateTimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.glabaystudios.network.dto.BookDTO;
import xyz.glabaystudios.network.dto.BookShelfDTO;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookShelfService implements DateTimeUtils {
    private final BookShelfRepository bookShelfRepository;
    private final BookService bookService;

    public List<BookShelfDTO> getBookShelvesForUser(Long discordId) {
        var bookShelf = bookShelfRepository.findByOwnerDiscordId(discordId);
        if (Objects.nonNull(bookShelf)) {
            return bookShelf.stream()
                    .filter(Objects::nonNull)
                    .map(this::convertObjectToDTO)
                    .collect(Collectors.toList());
        }
        return null;
    }

    public BookShelf getBookshelfForId(Long shelfId) {
        var shelf = bookShelfRepository.findByShelfId(shelfId);
        return shelf.orElse(null);
    }

    public void saveShelf(BookShelf model) {
        bookShelfRepository.save(model);
    }

    public void addBookToShelf(BookShelfDTO shelf, BookDTO book) {
        book.setShelfId(shelf.getShelfId());
        if (bookService.updateBook(book))
            System.out.println("Successfully added book to shelf.");
        else throw new RuntimeException("Error adding book to shelf");
    }

    public BookShelf updateBookShelf(BookShelf shelf, BookShelfDTO dto) {
        shelf.setShelfName(dto.getShelfName());
        shelf.setShelfDescription(dto.getShelfDescription());
        shelf.setOwnerDiscordId(dto.getOwnerDiscordId());
        shelf.setUpdatedOn(getCurrentDateAndTime());
        return shelf;
    }

    public BookShelf createNewBookShelf(String name, String description, Long ownerDiscordId) {
        BookShelf newShelf = new BookShelf();
        newShelf.setShelfName(name);
        newShelf.setShelfDescription(description);
        newShelf.setOwnerDiscordId(ownerDiscordId);
        newShelf.setCreatedOn(getCurrentDateAndTime());
        newShelf.setUpdatedOn(getCurrentDateAndTime());
        return newShelf;
    }

    private BookShelfDTO convertObjectToDTO(BookShelf model) {
        BookShelfDTO dto = new BookShelfDTO();
            dto.setShelfId(model.getUid());
            dto.setOwnerDiscordId(model.getOwnerDiscordId());
            dto.setShelfName(model.getShelfName());
            dto.setShelfDescription(model.getShelfDescription());
        return dto;
    }
}
