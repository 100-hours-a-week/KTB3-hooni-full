package kakaotech.community.global.page;

import java.util.List;

public record PageResult<T>(
    List<T> elements,
    int pageNum,
    int pageSize,
    int totalPage,
    int totalSize
) {
}
