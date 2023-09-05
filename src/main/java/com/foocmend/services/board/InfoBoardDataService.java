package com.foocmend.services.board;

import com.foocmend.commons.ListData;
import com.foocmend.commons.MemberUtil;
import com.foocmend.commons.Pagination;
import com.foocmend.commons.Utils;
import com.foocmend.controllers.board.BoardSearch;
import com.foocmend.entities.Board;
import com.foocmend.entities.BoardData;
import com.foocmend.entities.FileInfo;
import com.foocmend.entities.QBoardData;
import com.foocmend.repositories.BoardDataRepository;
import com.foocmend.services.board.config.InfoBoardConfigService;
import com.foocmend.services.file.InfoFileService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InfoBoardDataService {
    private final BoardDataRepository boardDataRepository;
    private final InfoBoardConfigService configInfoService;
    private final MemberUtil memberUtil;
    private final InfoFileService infoFileService;

    private final EntityManager em;
    private final HttpServletRequest request;

    public BoardData get(Long id) {
        return get(id, "view");
    }

    public BoardData get(Long id, String location) {

        BoardData boardData = boardDataRepository.findById(id).orElseThrow(BoardDataNotExistsException::new);

        // 게시판 설정 조회 + 접근 권한체크
        configInfoService.get(boardData.getBoard().getBId(), location);

        // 게시글 삭제 여부 체크(소프트 삭제)
        if (!memberUtil.isAdmin() && boardData.getDeletedDt() != null) {
            throw new BoardDataNotExistsException();
        }

        /** 게시글 에디터 및 첨부 파일 추가 */
        addFileInfo(boardData);

        return boardData;
    }

    /*
    * 게시글 목록 조회
    *
    */
    public ListData<BoardData> getList(String bId, BoardSearch search) {

        // 게시판 설정 조회 + 접근 권한체크
        Board board = configInfoService.get(bId, "list");

        int page = Utils.getNumber(search.getPage(), 1);
        int limit = Utils.getNumber(board.getRowsOfPage(), 20);
        if (search.getLimit() > 0) limit = search.getLimit();

        int offset = (page - 1) * limit;

        QBoardData boardData = QBoardData.boardData;

        /* 검색 처리 S */
        BooleanBuilder andBuilder = new BooleanBuilder();
        andBuilder.and(boardData.board.bId.eq(bId)) // 게시판 아이디
                .and(boardData.deletedDt.isNull()); // 미삭제 게시글만 조회

        /** 회원이 작성한 게시글 조회 */
        String email = search.getEmail();
        if (email != null && !email.isBlank()) {
            andBuilder.and(boardData.member.email.eq(email));
        }

        /* 검색 처리 E */
        String sopt = search.getSopt();
        String skey = search.getSkey();
        if (sopt != null && !sopt.isBlank() && skey != null && !skey.isBlank()) {
            sopt = sopt.trim();
            skey = skey.trim();

            if (sopt.equals("all")) { // 통합검색
                BooleanBuilder orBuilder = new BooleanBuilder();
                orBuilder.or(boardData.poster.contains(skey))
                        .or(boardData.member.nickname.contains(skey))
                        .or(boardData.member.email.contains(skey))
                        .or(boardData.subject.contains(skey))
                        .or(boardData.content.contains(skey));
                andBuilder.and(orBuilder);
            } else if (sopt.equals("subject")) { // 제목 검색
                andBuilder.and(boardData.subject.contains(skey));
            } else if (sopt.equals("content")) { // 내용 검색
                andBuilder.and(boardData.content.contains(skey));
            } else if (sopt.equals("subject_content")) { // 제목 + 내용 검색
                BooleanBuilder orBuilder = new BooleanBuilder();
                orBuilder.or(boardData.subject.contains(skey))
                        .or(boardData.content.contains(skey));
                andBuilder.and(orBuilder);
            } else if (sopt.equals("poster")) { // 작성자 검색
                BooleanBuilder orBuilder = new BooleanBuilder();
                orBuilder.or(boardData.poster.contains(skey))
                        .or(boardData.member.nickname.contains(skey))
                        .or(boardData.member.email.contains(skey));
                andBuilder.and(orBuilder);
            }
        }
        /** 정렬 처리 S */

        List<OrderSpecifier> orderSpecifier = new ArrayList<>();
        String sort = search.getSort();
        if (sort == null || sort.isBlank()) {
            sort = "createdDt_DESC"; // 기본 정렬은 최신 게시글 순
        }

        List<String[]> sorts = Arrays.stream(sort.trim().split(","))
                    .map(s -> s.split("_")).toList();
        PathBuilder pathBuilder = new PathBuilder(BoardData.class, "boardData");
        for (String[] _sort : sorts) {
            Order direction = Order.valueOf(_sort[1].toUpperCase()); // 정렬 방향
            orderSpecifier.add(new OrderSpecifier(direction, pathBuilder.get(_sort[0])));
        }

        /** 정렬 처리 E */

        JPAQueryFactory factory = new JPAQueryFactory(em);
        List<BoardData> items = factory.selectFrom(boardData)
                .leftJoin(boardData.member)
                .fetchJoin()
                .where(andBuilder)
                .offset(offset)
                .limit(limit)
                .orderBy(orderSpecifier.toArray(OrderSpecifier[]::new))
                .fetch();

        /** 게시글 에디터 및 첨부 파일 추가 */
        items.stream().forEach(this::addFileInfo);

        ListData<BoardData> data = new ListData<>();
        data.setContent(items);
        data.setExtra(board);

        int total = (int)boardDataRepository.count(andBuilder);
        Pagination pagination = new Pagination(page, total, 10, limit, request);
        data.setPagination(pagination);

        return data;
    }

    public ListData<BoardData> getList(String bId) {
        return getList(bId, null);
    }

    /**
     * 게시글 에디터 및 첨부 파일 추가
     * @param boardData
     */
    public void addFileInfo(BoardData boardData) {
        String gid = boardData.getGid();
        List<FileInfo> editorImages = infoFileService.getListDone(gid, "editor");
        List<FileInfo> attachFiles = infoFileService.getListDone(gid, "attach");

        boardData.setEditorImages(editorImages);
        boardData.setAttachFiles(attachFiles);
    }

    /**
     * 로그인한 회원이 작성한 게시글 조회
     *
     * @param bId
     * @param page
     * @param limit
     * @return
     */
    public ListData<BoardData> getMine(String bId, int page, int limit) {
        if (!memberUtil.isLogin()) {
            return null;
        }

        BoardSearch search = new BoardSearch();
        page = Utils.getNumber(page, 1);
        limit = Utils.getNumber(limit, 20);
        search.setPage(page);
        search.setLimit(limit);
        search.setEmail(memberUtil.getMember().getEmail());

        return getList(bId, search);
    }
}
