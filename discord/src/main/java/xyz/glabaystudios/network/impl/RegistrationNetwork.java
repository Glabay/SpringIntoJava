package xyz.glabaystudios.network.impl;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.impl.client.CloseableHttpClient;
import xyz.glabaystudios.network.GlabayStudiosNetwork;
import xyz.glabaystudios.network.dto.RegistrationFormDTO;

import java.io.IOException;
import java.util.Objects;

public interface RegistrationNetwork extends GlabayStudiosNetwork {

    default Boolean checkIfUserExists(RegistrationFormDTO dto) {
        try (CloseableHttpClient httpClient = getHttpClient()) {
            HttpResponse response = submitHttpPostWithBodyAwaitReply(BASE_API_ENDPOINT.concat("/v1/registry/registered"), getStringEntityFromDTO(dto), httpClient);
            if (Objects.nonNull(response)) {
                int status = response.getStatusLine().getStatusCode();
                if (Objects.equals(status, HttpStatus.SC_CREATED)) {
                    return Boolean.TRUE;
                }
                if (Objects.equals(status, HttpStatus.SC_CONFLICT)) {
                    return Boolean.FALSE;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    default String createNewAccount(RegistrationFormDTO dto) {
        try (CloseableHttpClient httpClient = getHttpClient()) {
            HttpResponse response = submitHttpPostWithBodyAwaitReply(BASE_API_ENDPOINT.concat("/v1/registry/create"), getStringEntityFromDTO(dto), httpClient);
            if (Objects.nonNull(response)) {
                int status = response.getStatusLine().getStatusCode();
                if (Objects.equals(status, HttpStatus.SC_CREATED))
                    return "Successfully Created account";
                else
                    return "Something went Wrong...";

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
