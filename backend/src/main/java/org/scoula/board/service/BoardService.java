package org.scoula.board.service;

import java.util.List;
import org.scoula.board.domain.BoardAttachmentVO;
import org.scoula.board.dto.BoardDTO;

public interface BoardService {

  public List<BoardDTO> getList();

  public BoardDTO get(Long no);

  public BoardDTO create(BoardDTO board);

  public BoardDTO update(BoardDTO board);

  public BoardDTO delete(Long no);

  // 첨부파일 관련 메서드 추가
  public BoardAttachmentVO getAttachment(Long no);

  public boolean deleteAttachment(Long no);
}
