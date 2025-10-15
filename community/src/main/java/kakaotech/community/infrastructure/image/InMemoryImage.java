package kakaotech.community.infrastructure.image;

public record InMemoryImage(
    byte[] bytes,
    String contentType,
    long size
){
}
