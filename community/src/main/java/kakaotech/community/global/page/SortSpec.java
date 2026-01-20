package kakaotech.community.global.page;

public record SortSpec(
        String property,
        Direction direction
) {
    public enum Direction {
        ASC, DESC
    }
}
