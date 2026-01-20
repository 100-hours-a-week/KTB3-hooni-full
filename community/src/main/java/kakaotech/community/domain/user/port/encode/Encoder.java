package kakaotech.community.domain.user.port.encode;

public interface Encoder {
    String encode(String rawPassword);

    boolean matches(String rawPassword, String encodedPassword);
}
