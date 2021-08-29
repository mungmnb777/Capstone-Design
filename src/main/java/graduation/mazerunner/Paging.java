package graduation.mazerunner;

import lombok.Getter;

@Getter
public class Paging {

    // 페이지 당 게시글 수
    private int postPerPage = 10;

    // 한 블록당 페이지네이션 버튼 개수
    private int blockSize = 10;

    // 현재 페이지
    private int page = 1;

    // 현재 블록
    private int block = 1;

    // 총 게시글 수
    private int totalPostCnt;

    // 총 페이지 수
    private int totalPageCnt;

    // 총 블록 수
    private int totalBlockCnt;

    // 시작 페이지
    private int startPage = 1;

    // 종료 페이지
    private int endPage = 1;

    // DB 검색 시작 지점
    private int startIndex = 0;

    // 이전 블록
    private int prevBlock;

    // 다음 블록
    private int nextBlock;

    public Paging(int totalPostCnt, int page) {

        this.page = page;

        this.totalPostCnt = (totalPostCnt == 0) ? 1 : totalPostCnt;

        this.totalPageCnt = (int)Math.ceil(this.totalPostCnt * 1.0 / postPerPage);

        this.totalBlockCnt = (int)Math.ceil(totalPageCnt * 1.0 / blockSize);

        this.block = (int)Math.ceil(page * 1.0 / blockSize);

        this.startPage = (block - 1) * blockSize + 1;

        this.endPage = block * blockSize;

        if (endPage > totalPageCnt) { this.endPage = totalPageCnt; }

        this.prevBlock = (block * blockSize) - blockSize;

        if(prevBlock < 1) { this.prevBlock = 1; }

        this.nextBlock = (block * blockSize) + 1;

        if(nextBlock > totalPageCnt) { this.nextBlock = totalPageCnt; }

        this.startIndex = (this.page - 1) * postPerPage;
    }
}
