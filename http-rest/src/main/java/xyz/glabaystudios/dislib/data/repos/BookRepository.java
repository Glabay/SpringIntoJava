package xyz.glabaystudios.dislib.data.repos;

import xyz.glabaystudios.dislib.data.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByISBN10(Long ISBN10);
    Optional<Book> findByISBN13(Long ISBN13);
    Optional<Book> getByShelfIdAndOwnerDiscordId(Long aLong, Long ownerDiscordId);

    Collection<Book> findByShelfIdAndOwnerDiscordId(Long shelfId, Long ownerDiscordId);
    Collection<Book> findByShelfId(Long shelfId);

    Collection<Book> findByShelfIdLessThanAndOwnerDiscordId(Long shelfId, Long ownerDiscordId);
}