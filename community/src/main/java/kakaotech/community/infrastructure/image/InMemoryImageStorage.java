package kakaotech.community.infrastructure.image;

import kakaotech.community.domain.image.ImageStorage;
import kakaotech.community.domain.image.dto.ImageMeta;
import kakaotech.community.global.exception.ImageException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static kakaotech.community.global.exception.code.ExceptionCode.FAILED_TO_UPLOAD_IMAGE;
import static kakaotech.community.global.exception.code.ExceptionCode.IMAGE_NOT_FOUND;

@Component
public class InMemoryImageStorage implements ImageStorage {
    private final Map<UUID, InMemoryImage> imageDatabase = new ConcurrentHashMap<>();

    @Override
    public synchronized UUID upload(UUID id, MultipartFile file) {
        InMemoryImage image = new InMemoryImage(
                toByteArray(file),
                Optional.ofNullable(file.getContentType()).orElse("application/octet-stream"),
                file.getSize()
        );

        imageDatabase.put(id, image);
        return id;
    }

    @Override
    public ImageMeta getImage(UUID uuid) {
        InMemoryImage image = Optional.ofNullable(imageDatabase.get(uuid))
                .orElseThrow(() -> new ImageException(IMAGE_NOT_FOUND));

        return new ImageMeta(new ByteArrayResource(image.bytes()), MediaType.parseMediaType(image.contentType()), image.size());
    }

    @Override
    public synchronized void delete(UUID uuid) {
        imageDatabase.remove(uuid);
    }

    private byte[] toByteArray(MultipartFile file) {
        try {
            return file.getBytes();
        } catch (IOException e) {
            throw new ImageException(FAILED_TO_UPLOAD_IMAGE);

        }
    }
}
