package xyz.glabaystudios.network.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BookDTO {

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
