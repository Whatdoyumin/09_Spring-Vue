package org.scoula.board.service;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.board.domain.BoardAttachmentVO;
import org.scoula.board.domain.BoardVO;
import org.scoula.board.dto.BoardDTO;
import org.scoula.board.mapper.BoardMapper;
import org.scoula.common.pagination.Page;
import org.scoula.common.pagination.PageRequest;
import org.scoula.common.util.UploadFiles;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Log4j2
@Service
@RequiredArgsConstructor        // final 멤버를 인자로 가지는 생성자
public class BoardServiceImpl implements BoardService {

  private final static String BASE_DIR = "/Users/whatdoyumin/upload/board";
  private final BoardMapper boardMapper;

  @Override
  public List<BoardDTO> getList() {
    log.info("getList............");

    return boardMapper.getList().stream() // BoardVO의 스트림
        .map(BoardDTO::of) // BoardDTO의 스트림
        .toList();         // List<BoardDTO> 변환
  }

  @Override
  public BoardDTO get(Long no) {
    log.info("get......." + no);

    BoardDTO board = BoardDTO.of(boardMapper.get(no));

    return Optional.ofNullable(board)
        .orElseThrow(NoSuchElementException::new);
  }

  // 게시글 등록 서비스
  @Transactional      // 여러 DB 작업을 하나의 트랜잭션으로 처리
  @Override
  public BoardDTO create(BoardDTO dto) {
    log.info("create........" + dto);

    // 1. 게시글 등록
    BoardVO vo = dto.toVo();         // DTO → VO 변환
    boardMapper.create(vo);            // DB에 저장
    dto.setNo(vo.getNo());           // 생성된 PK를 DTO에 설정

    // 2. 첨부파일 처리
    List<MultipartFile> files = dto.getFiles();
    if (files != null && !files.isEmpty()) {
      upload(vo.getNo(), files);  // 게시글 번호가 필요하므로 게시글 등록 후 처리
    }

    return get(vo.getNo());
  }

  @Override
  public BoardDTO update(BoardDTO board) {
    log.info("update...... " + board);
    BoardVO boardVO = board.toVo();
    log.info("update...... " + boardVO);
    boardMapper.update(boardVO);

    // 파일 업로드 처리
    List<MultipartFile> files = board.getFiles();
    if (files != null && !files.isEmpty()) {
      upload(board.getNo(), files);
    }
    return get(board.getNo());
  }

  @Override
  public BoardDTO delete(Long no) {
    log.info("delete........." + no);
    BoardDTO board = get(no);

    boardMapper.delete(no);
    return board;
  }

  // 파일 첨부 관련 메서드 추가

  // 첨부파일 단일 조회
  @Override
  public BoardAttachmentVO getAttachment(Long no) {
    return boardMapper.getAttachment(no);
  }

  // 첨부파일 삭제
  @Override
  public boolean deleteAttachment(Long no) {
    return boardMapper.deleteAttachment(no) == 1;
  }

  /**
   * 파일 업로드 처리 (private 메서드)
   *
   * @param bno   게시글 번호
   * @param files 업로드할 파일 목록
   */
  private void upload(Long bno, List<MultipartFile> files) {
    for (MultipartFile part : files) {
      // 빈 파일은 건너뛰기
      if (part.isEmpty()) {
        continue;
      }

      try {
        // 파일을 서버에 저장
        String uploadPath = UploadFiles.upload(BASE_DIR, part);

        // 첨부파일 정보를 DB에 저장
        BoardAttachmentVO attach = BoardAttachmentVO.of(part, bno, uploadPath);
        boardMapper.createAttachment(attach);

      } catch (IOException e) {
        // @Transactional이 감지할 수 있도록 RuntimeException으로 변환
        throw new RuntimeException(e);
      }
    }
  }

  @Override
  public Page<BoardDTO> getPage(PageRequest pageRequest) {
    List<BoardVO> boards = boardMapper.getPage(pageRequest);
    int totalCont = boardMapper.getTotalCount();

    return Page.of(pageRequest, totalCont,
        boards.stream().map(BoardDTO::of).toList());
  }
}
