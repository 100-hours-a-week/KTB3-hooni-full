package kakaotech.community.global.entity;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public abstract class BaseEntity {
    private final LocalDateTime createdAt;

    protected BaseEntity(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
