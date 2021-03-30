package tech.itpark.http;

public interface Listener<F, T> {
    void run(F request, T response);
}