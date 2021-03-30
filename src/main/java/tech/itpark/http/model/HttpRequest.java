package tech.itpark.http.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class HttpRequest {

    private String uri;
    private String header; //GET, POST
    private String body;
    private String version;
    private List<String> headers;
}
