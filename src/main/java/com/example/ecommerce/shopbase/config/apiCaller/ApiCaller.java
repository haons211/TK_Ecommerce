package com.example.ecommerce.shopbase.config.apiCaller;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class ApiCaller {

    public static String callApi(String url, Map<String, String> headers, Object requestObject) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            // Tạo builder cho request
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(url));

            // Thêm headers
            headers.forEach(requestBuilder::header);

            ObjectMapper mapper = new ObjectMapper();
            String requestBody= mapper.writeValueAsString(requestObject);

            // Nếu có request body, thêm vào (cho POST requests)
            if (requestBody != null && !requestBody.isEmpty()) {
                requestBuilder.POST(HttpRequest.BodyPublishers.ofString(requestBody));
            } else {
                requestBuilder.GET();
            }

            HttpRequest request = requestBuilder.build();

            // Gửi request và nhận response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Trả về body của response
            return response.body();

        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
}