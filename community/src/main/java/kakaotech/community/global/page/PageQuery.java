package kakaotech.community.global.page;

public record PageQuery(
        int pageNum,
        int pageSize,
        SortSpec sort
) {
    public PageQuery(int pageNum) {
        this(pageNum, 20, new SortSpec("createdAt", SortSpec.Direction.DESC));
    }
}
