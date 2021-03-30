package tech.itpark.http;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        final var server = new Server();
        server.get("/path", (request, response) -> {
            response.setStatus(200);
            System.out.println(request.getUri());
        });

        server.post("/path", (request, response) -> {
            System.out.println(request.getBody());
            request.setBody(response.getBody());
            response.setStatus(200);
        });
        server.listen(9999);
    }
}
