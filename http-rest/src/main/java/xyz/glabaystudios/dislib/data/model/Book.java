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
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long uid;

    private String createdOn;
    private String updatedOn;

    private String title;
    private String author;
    private String theme;
    private String description;
    private String publisher;
    private String publishedDate;

    private Long shelfId;
    private Long ownerDiscordId;
    private Long ISBN10;
    private Long ISBN13;
}
