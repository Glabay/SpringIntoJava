package xyz.glabaystudios.network.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpStatus;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import xyz.glabaystudios.network.GlabayStudiosNetwork;
import xyz.glabaystudios.network.dto.BookDTO;
import xyz.glabaystudios.network.dto.BookShelfDTO;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public interface BookShelfNetwork extends GlabayStudiosNetwork {

    default String postCreateNewBookShelf(BookShelfDTO dto) {
        try (CloseableHttpClient httpClient = getHttpClient()) {
            var response = submitHttpPostWithBodyAwaitReply(BASE_API_ENDPOINT.concat("/v1/bookshelf/create"), getStringEntityFromDTO(dto), httpClient);
            if (Objects.nonNull(response))
                return EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    default String postUpdateNewBookShelf(BookShelfDTO dto) {
        try (CloseableHttpClient httpClient = getHttpClient()) {
            var response = submitHttpPostWithBodyAwaitReply(BASE_API_ENDPOINT.concat("/v1/bookshelf/update"), getStringEntityFromDTO(dto), httpClient);
            if (Objects.nonNull(response))
                return EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    default List<BookShelfDTO> getBookshelvesForUser(Long discordUserId) {
        try {
            var response = fetchHttpGetResponse(BASE_API_ENDPOINT.concat("/v1/bookshelf/fetch/shelves/").concat(discordUserId.toString()), getHttpClient());
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
                return null;
            else
                return new ObjectMapper().readValue(EntityUtils.toString(response.getEntity(), "UTF-8"), new TypeReference<>() {});

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    default List<BookDTO> getBooksForUserNotOnShelf(Long discordUserId) {
        try {
            var response = fetchHttpGetResponse(BASE_API_ENDPOINT.concat("/v1/book/all/no-shelf/").concat(discordUserId.toString()), getHttpClient());
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
                return null;
            else
                return new ObjectMapper().readValue(EntityUtils.toString(response.getEntity(), "UTF-8"), new TypeReference<>() {});

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    default List<BookDTO> getBooksForUserOnShelf(Long discordUserId, Long shelfId) {
        try {
            var response = fetchHttpGetResponse(BASE_API_ENDPOINT.concat("/v1/book/all/%d/%d".formatted(shelfId, discordUserId)), getHttpClient());
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
                return null;
            else
                return new ObjectMapper().readValue(EntityUtils.toString(response.getEntity(), "UTF-8"), new TypeReference<>() {});

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
