package kakaotech.community.domain.common.image;

import kakaotech.community.global.exception.ImageException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

import static kakaotech.community.global.exception.code.ExceptionCode.EMPTY_IMAGE;
import static kakaotech.community.global.exception.code.ExceptionCode.IS_NOT_IMAGE_EXTENSION;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageStorage imageStorage;

    public UUID save(MultipartFile file) {
        if (file.isEmpty()) {
            throw new ImageException(EMPTY_IMAGE);
        }

        String contentType = Optional.ofNullable(file.getContentType()).orElse("application/octet-stream");
        if (!contentType.startsWith("image/")) {
            throw new ImageException(IS_NOT_IMAGE_EXTENSION);
        }

        UUID uuid = UUID.randomUUID();
        return imageStorage.upload(uuid, file);
    }

    public Resource load(UUID uuid) {
        return imageStorage.getImage(uuid);
    }
}
