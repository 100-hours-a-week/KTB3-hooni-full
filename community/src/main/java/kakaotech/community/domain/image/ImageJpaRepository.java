package kakaotech.community.domain.image;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ImageJpaRepository extends JpaRepository<Image, UUID> {

    Optional<Image> findById(UUID id);

    void deleteById(UUID id);
}
