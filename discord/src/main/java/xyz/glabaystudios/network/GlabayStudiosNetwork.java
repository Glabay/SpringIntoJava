package xyz.glabaystudios.network;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public interface GlabayStudiosNetwork {

    String BASE_API_ENDPOINT = "http://localhost:8080/api";

    default CloseableHttpClient getHttpClient() {
        return HttpClientBuilder.create().build();
    }
    default String getObjectAsJson(Object object) throws JsonProcessingException {
        return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(object);
    }

    default StringEntity getStringEntityFromDTO(Object dataTransferObject) {
        try {
            return new StringEntity(getObjectAsJson(dataTransferObject));
        } catch (UnsupportedEncodingException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private HttpPost getHttpPost(String url) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Accept", "application/json");
        httpPost.addHeader("Content-type", "application/json");
        return httpPost;
    }

    default HttpPost getHttpPostWithBody(String url, StringEntity body) {
        HttpPost httpPost = getHttpPost(url);
        httpPost.setEntity(body);
        return httpPost;
    }

    default HttpResponse submitHttpPostWithReply(String url, CloseableHttpClient httpClient) throws IOException {
        return httpClient.execute(getHttpPost(url));
    }
    default HttpResponse submitHttpPostWithBodyAwaitReply(String url, StringEntity body, CloseableHttpClient httpClient) throws IOException {
        return httpClient.execute(getHttpPostWithBody(url, body));
    }

    private HttpGet getHttpGet(String url) {
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("Accept", "application/json");
        httpGet.addHeader("Content-type", "application/json");
        return httpGet;
    }

    default HttpResponse fetchHttpGetResponse(String url, CloseableHttpClient httpClient) throws IOException {
        return httpClient.execute(getHttpGet(url));
    }
}
