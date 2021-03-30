package tech.itpark.http.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class HttpResponse {

    private String statusText;
    private int status;
    private String body;
    private List<String> headers;

}
