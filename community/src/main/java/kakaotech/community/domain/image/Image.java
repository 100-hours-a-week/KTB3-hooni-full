package kakaotech.community.domain.image;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class Image {
    @Id
    UUID id;

    @Column(name = "bytes", columnDefinition = "LONGBLOB", nullable = false)
    byte[] bytes;

    String mediaType;

    long size;
}
