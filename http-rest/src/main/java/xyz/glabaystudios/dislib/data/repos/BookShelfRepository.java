package xyz.glabaystudios.dislib.data.repos;

import xyz.glabaystudios.dislib.data.model.BookShelf;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;

public interface BookShelfRepository extends JpaRepository<BookShelf, Long> {
    Optional<BookShelf> findByShelfId(Long shelfId);
    Collection<BookShelf> findByOwnerDiscordId(Long ownerDiscordId);
}