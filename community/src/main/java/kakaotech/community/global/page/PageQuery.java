package kakaotech.community.global.page;

public record PageQuery(
        int pageNum,
        int pageSize,
        SortSpec sort
) {
    public static PageQuery offset(int pageNum) {
        return new PageQuery(pageNum, 20, new SortSpec("createdAt", SortSpec.Direction.DESC));
    }

    public static PageQuery cursor() {
        return new PageQuery(0, 20, new SortSpec("createdAt", SortSpec.Direction.DESC));
    }
}
