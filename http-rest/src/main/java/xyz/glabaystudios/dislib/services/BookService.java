package xyz.glabaystudios.dislib.services;

import xyz.glabaystudios.dislib.data.model.Book;
import xyz.glabaystudios.dislib.data.repos.BookRepository;
import xyz.glabaystudios.dislib.util.DateTimeUtils;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import xyz.glabaystudios.network.dto.BookDTO;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService implements DateTimeUtils {
    private final BookRepository bookRepository;
    private final HttpService httpService;

    public void save(Book book) {
        bookRepository.save(book);
    }

    public boolean updateBook(BookDTO dto) {
        var book = bookRepository.getByShelfIdAndOwnerDiscordId(dto.getShelfId(), dto.getOwnerDiscordId());
        if (book.isPresent()) {
            Book updatedBook = updateBookObject(book.get(), dto);
            bookRepository.save(updatedBook);
            return true;
        }
        return false;
    }

    public List<BookDTO> findAllForShelfIdForUser(Long shelfId, Long discordId) {
        return bookRepository.findByShelfIdAndOwnerDiscordId(shelfId, discordId).stream().map(this::convertObjectToDTO).collect(Collectors.toList());
    }

    public BookDTO getBookDtoForIsbn10(Long isbn10) {
        var book = bookRepository.findByISBN10(isbn10);
        return book.map(this::convertObjectToDTO).orElse(null);
    }

    public BookDTO getBookDtoForIsbn13(Long isbn13) {
        var book = bookRepository.findByISBN10(isbn13);
        return book.map(this::convertObjectToDTO).orElse(null);
    }

    public List<BookDTO> findAllForShelfId(Long shelfId) {
        return bookRepository.findByShelfId(shelfId).stream().map(this::convertObjectToDTO).collect(Collectors.toList());
    }

    public List<BookDTO> findAll() {
        return bookRepository.findAll().stream().map(this::convertObjectToDTO).collect(Collectors.toList());
    }

    public BookDTO parseBookDataObject(String data) {
        System.out.println(data);
        BookDTO dto = new BookDTO();
        try {
            // Parse the inti data
            Object object = new JSONParser().parse(data);
            JSONObject bookData = (JSONObject) object;
            dto.setTitle(bookData.get("title").toString());
            if (Objects.nonNull(bookData.get("subtitle")))
                dto.setDescription(bookData.get("subtitle").toString());
            else if (Objects.nonNull(bookData.get("first_sentence")))
                dto.setDescription(bookData.get("first_sentence").toString());

            // Parse Publisher
            Object publisherData = new JSONParser().parse(bookData.get("publishers").toString());
            JSONArray publishersArray = (JSONArray) publisherData;
            dto.setPublisher(publishersArray.get(0).toString());

            // Parse isbn13
            Object isbn13Data = new JSONParser().parse(bookData.get("isbn_13").toString());
            JSONArray isbn13Array = (JSONArray) isbn13Data;
            dto.setISBN13(Long.parseLong(isbn13Array.get(0).toString()));

            // Parse Author
            Object authorData = new JSONParser().parse(bookData.get("authors").toString());
            JSONArray authorArray = (JSONArray) authorData;
            Object authorObject = new JSONParser().parse(authorArray.get(0).toString());
            JSONObject authorJson = (JSONObject) authorObject;
            String[] authorsData = authorJson.get("key").toString().split("/");
            var author = fetchAuthor(authorsData[1], authorsData[2]);
            dto.setAuthor(author);

            // Parse book details
            String[] bookDetails = bookData.get("key").toString().split("/");
            var publishedDate = fetchPublishedDate(bookDetails[1], bookDetails[2]);
            dto.setPublishedDate(publishedDate);

        } catch(ParseException e) {
            throw new RuntimeException(e);
        }
        return dto;
    }
    private String fetchPublishedDate(String api, String key) {
        var response = httpService.submitHttpGet(api, key, httpService.getHttpClient());
        try {
            Object object = new JSONParser().parse(response);
            JSONObject jsonObject = (JSONObject) object;
            return jsonObject.get("publish_date").toString();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private String fetchAuthor(String api, String key) {
        var response = httpService.submitHttpGet(api, key, httpService.getHttpClient());
        try {
            Object object = new JSONParser().parse(response);
            JSONObject jsonObject = (JSONObject) object;
            return jsonObject.get("name").toString();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private Book updateBookObject(Book model, BookDTO dto) {
        model.setTitle(dto.getTitle());
        model.setAuthor(dto.getAuthor());
        model.setTheme(dto.getTheme());
        model.setDescription(dto.getDescription());
        model.setPublisher(dto.getPublisher());
        model.setPublishedDate(dto.getPublishedDate());
        model.setShelfId(dto.getShelfId());
        model.setOwnerDiscordId(dto.getOwnerDiscordId());
        model.setISBN10(dto.getISBN10());
        model.setISBN13(dto.getISBN13());
        model.setUpdatedOn(getCurrentDateAndTime());
        return model;
    }

    public Book createNewBookObject(BookDTO dto) {
        Book newBook = new Book();
        newBook.setTitle(dto.getTitle());
        newBook.setAuthor(dto.getAuthor());
        newBook.setTheme(dto.getTheme());
        newBook.setDescription(dto.getDescription());
        newBook.setPublisher(dto.getPublisher());
        newBook.setPublishedDate(dto.getPublishedDate());
        newBook.setShelfId(newBook.getUid());
        newBook.setOwnerDiscordId(dto.getOwnerDiscordId());
        newBook.setISBN10(dto.getISBN10());
        newBook.setISBN13(dto.getISBN13());
        newBook.setCreatedOn(getCurrentDateAndTime());
        newBook.setUpdatedOn(getCurrentDateAndTime());

        return newBook;
    }

    private BookDTO convertObjectToDTO(Book object) {
        BookDTO dto = new BookDTO();
        dto.setTitle(object.getTitle());
        dto.setAuthor(object.getAuthor());
        dto.setTheme(object.getTheme());
        dto.setDescription(object.getDescription());
        dto.setPublisher(object.getPublisher());
        dto.setPublishedDate(object.getPublishedDate());
        dto.setShelfId(object.getShelfId());
        dto.setOwnerDiscordId(object.getOwnerDiscordId());
        dto.setISBN10(object.getISBN10());
        dto.setISBN13(object.getISBN13());

        return dto;
    }
}
