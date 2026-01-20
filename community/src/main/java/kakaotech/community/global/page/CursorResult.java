package kakaotech.community.global.page;

import java.util.List;

public record CursorResult<T>(
        List<T> elements,
        boolean hasNext
) {
}
