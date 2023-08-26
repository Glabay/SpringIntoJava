package xyz.glabaystudios.network.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
public class BookShelfDTO {

    private Long shelfId;
    private Long ownerDiscordId;
    private String shelfName;
    private String shelfDescription;

    private Collection<BookDTO> books = new ArrayList<>();
}
