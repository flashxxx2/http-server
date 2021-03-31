package tech.itpark.http;

import tech.itpark.http.exception.*;
import tech.itpark.http.guava.Bytes;
import tech.itpark.http.model.HttpRequest;
import tech.itpark.http.model.HttpResponse;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class Server {
  public static final String GET = "GET";
  public static final String POST = "POST";
  public static final Set<String> allowedMethods = Set.of(GET, POST);
  public static final Set<String> supportedHTTPVersions = Set.of("HTTP/1.1");
  private String getPath;
  public Listener<HttpRequest, HttpResponse> getHandle;
  public Listener<HttpRequest, HttpResponse> postHandle;

  public void get(String path, Listener<HttpRequest, HttpResponse> getListener) {
    getPath = path;
    getHandle = getListener;
  }

  public void post(String path, Listener<HttpRequest, HttpResponse> postListener) {
    getPath = path;
    postHandle = postListener;
  }

  public void listen(int port) {
    try (
        final var serverSocket = new ServerSocket(port);
    ) {
      while (true) {
        try {
          final var socket = serverSocket.accept();
          handleConnection(socket);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void handleConnection(Socket socket) {
    try (
        socket;
        final var in = new BufferedInputStream(socket.getInputStream());
        final var out = new BufferedOutputStream(socket.getOutputStream());
    ) {
      try {
        final var buffer = new byte[4096];
        in.mark(4096);

        final var read = in.read(buffer);
        final var CRLF = new byte[]{'\r', '\n'};

        final var requestLineEndIndex = Bytes.indexOf(buffer, CRLF, 0, read) + CRLF.length;
        if (requestLineEndIndex == -1) {
          throw new MalFormedRequestException();
        }

        final var requestLineParts = new String(buffer, 0, requestLineEndIndex)
            .trim()
            .split(" ");

        if (requestLineParts.length != 3) {
          throw new InvalidRequestLineException();
        }

        final var method = requestLineParts[0];
        final var uri = requestLineParts[1];
        final var version = requestLineParts[2];

        if (!allowedMethods.contains(method)) {
          throw new InvalidHeaderException("invalid method: " + method);
        }

        if (!uri.startsWith("/")) {
          throw new InvalidUriException("invalid uri: " + uri);
        }

        if (!supportedHTTPVersions.contains(version)) {
          throw new InvalidVersionException("invalid version: " + version);
        }

        if (method.equals(GET) && getHandle != null) {
          if(uri.equals(getPath)) {
            getHandle.run(new HttpRequest(uri, "хедер", "body", version, Collections.singletonList("headers")),
                    new HttpResponse("text", 200, "body", Collections.singletonList("headers")));
          }
//          getRequest(out, "OK");
          return;
        }

        final var CRLFCRLF = new byte[]{'\r', '\n', '\r', '\n'};
        final var headersEndIndex = Bytes.indexOf(buffer, CRLFCRLF, requestLineEndIndex, read) + CRLFCRLF.length;

        if(postHandle != null && uri.equals(getPath)) {
          postHandle.run(new HttpRequest(uri, "хедер", "body", version, Collections.singletonList("headers")),
                  new HttpResponse("text", 200, "body", Collections.singletonList("headers")));
        }
//        postRequest(in, out, buffer, CRLF, requestLineEndIndex, headersEndIndex);

      } catch (MalFormedRequestException e) {
        e.printStackTrace();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void getRequest(BufferedOutputStream out, String ok) throws IOException {
    out.write(
            (
                    "HTTP/1.1 200 OK\r\n" +
                            "Content-Type: text/plain\r\n" +
                            "Content-Length: " + ok.length() + "\r\n" +
                            "Connection: close\r\n" +
                            "\r\n" +
                            ok
            ).getBytes());
  }

  private void postRequest(BufferedInputStream in, BufferedOutputStream out, byte[] buffer, byte[] CRLF, int requestLineEndIndex, int headersEndIndex) throws IOException {
    int contentLength;
    var lastIndex = requestLineEndIndex;
    while (true) {
      final var headerEndIndex = Bytes.indexOf(buffer, CRLF, lastIndex, headersEndIndex) + CRLF.length;
      if (headerEndIndex == -1) {
        throw new MalFormedRequestException();
      }
      final var headerLine = new String(buffer, lastIndex, headerEndIndex - lastIndex);

      if (headerLine.startsWith("Content-Length")) {
        final var headerParts = Arrays.stream(headerLine.split(":"))
            .map(String::trim)
            .collect(Collectors.toList());

        if (headerParts.size() != 2) {
          throw new RuntimeException("bad Content-Length: " + headerLine);
        }

        contentLength = Integer.parseInt(headerParts.get(1));
        break;
      }

      lastIndex = headerEndIndex;
    }
    in.reset();

    final var totalSize = headersEndIndex + contentLength;
    final var fullRequestBytes = in.readNBytes(totalSize);

    final var body = "Read: " + fullRequestBytes.length;
    out.write(
        (
            "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/plain\r\n" +
                "Content-Length: " + body.length() + "\r\n" +
                "Connection: close\r\n" +
                "\r\n" +
                body
        ).getBytes());
  }
}
