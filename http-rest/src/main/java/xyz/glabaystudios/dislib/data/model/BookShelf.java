package xyz.glabaystudios.dislib.data.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class BookShelf {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long uid;

    private String createdOn;
    private String updatedOn;

    private Long shelfId;
    private Long ownerDiscordId;
    private String shelfName;
    private String shelfDescription;

}
