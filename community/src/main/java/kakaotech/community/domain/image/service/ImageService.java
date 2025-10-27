package kakaotech.community.domain.image.service;

import kakaotech.community.domain.image.ImageStorage;
import kakaotech.community.domain.image.dto.ImageMeta;
import kakaotech.community.global.exception.ImageException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static kakaotech.community.global.exception.code.ExceptionCode.EMPTY_IMAGE;
import static kakaotech.community.global.exception.code.ExceptionCode.FAILED_TO_UPLOAD_IMAGE;
import static kakaotech.community.global.exception.code.ExceptionCode.IS_NOT_IMAGE_EXTENSION;

@Service
@RequiredArgsConstructor
public class ImageService {
    private static final int BUF_SIZE = 8192;

    private final ImageStorage imageStorage;

    public UUID save(MultipartFile file) {
        if (file.isEmpty()) {
            throw new ImageException(EMPTY_IMAGE);
        }

        String contentType = Optional.ofNullable(file.getContentType()).orElse("application/octet-stream");
        if (!contentType.startsWith("image/")) {
            throw new ImageException(IS_NOT_IMAGE_EXTENSION);
        }

        return imageStorage.upload(generateUUID(), file);
    }

    public ImageMeta load(UUID uuid) {
        return imageStorage.getImage(uuid);
    }

    public UUID updateImage(UUID uuid, MultipartFile file) {
        ImageMeta preImage = imageStorage.getImage(uuid);

        if (preImage.size() != file.getSize()) {
            imageStorage.delete(uuid);
            return imageStorage.upload(generateUUID(), file);
        }

        if (!compare(preImage.resource(), file)) {
            imageStorage.delete(uuid);
            return imageStorage.upload(generateUUID(), file);
        }

        return uuid;
    }

    public void delete(UUID uuid) {
        imageStorage.delete(uuid);
    }

    private UUID generateUUID() {
        return UUID.randomUUID();
    }

    private boolean compare(Resource resource, MultipartFile file) {
        try (InputStream in1 = resource.getInputStream();
             InputStream in2 = file.getInputStream()) {

            byte[] buf1 = new byte[BUF_SIZE];
            byte[] buf2 = new byte[BUF_SIZE];

            while (true) {
                int n1 = in1.read(buf1);
                int n2 = in2.read(buf2);

                if (n1 != n2) {
                    return false;
                }

                if (n1 == -1) {
                    return true;
                }

                if (Arrays.mismatch(buf1, 0, n1, buf2, 0, n2) != -1) {
                    return false;
                }
            }
        } catch (IOException e) {
            throw new ImageException(FAILED_TO_UPLOAD_IMAGE);
        }
    }
}
