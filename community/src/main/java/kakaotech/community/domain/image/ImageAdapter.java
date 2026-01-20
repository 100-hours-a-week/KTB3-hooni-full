package kakaotech.community.domain.image;

import kakaotech.community.domain.image.dto.ImageMeta;
import kakaotech.community.global.exception.ImageException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import static kakaotech.community.global.exception.code.ExceptionCode.FAILED_TO_UPLOAD_IMAGE;
import static kakaotech.community.global.exception.code.ExceptionCode.IMAGE_NOT_FOUND;

@Primary
@Component
@RequiredArgsConstructor
public class ImageAdapter implements ImageStorage {
    private final ImageJpaRepository imageJpaRepository;

    @Override
    public UUID upload(UUID id, MultipartFile file) {
        byte[] imageBytes = toByteArray(file);
        Image image = new Image(id, imageBytes,
                Optional.ofNullable(file.getContentType()).orElse("application/octet-stream"),
                file.getSize());

        imageJpaRepository.save(image);
        return id;
    }

    @Override
    public ImageMeta getImage(UUID imageId) {
        Image image = imageJpaRepository.findById(imageId)
                .orElseThrow(() -> new ImageException(IMAGE_NOT_FOUND));

        return new ImageMeta(new ByteArrayResource(image.getBytes()),
                MediaType.parseMediaType(image.getMediaType()),
                image.getSize());
    }

    @Override
    public void delete(UUID uuid) {
        imageJpaRepository.deleteById(uuid);
    }

    private byte[] toByteArray(MultipartFile file) {
        try {
            return file.getBytes();
        } catch (IOException e) {
            throw new ImageException(FAILED_TO_UPLOAD_IMAGE);

        }
    }
}
