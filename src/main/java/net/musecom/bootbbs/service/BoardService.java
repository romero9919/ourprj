package net.musecom.bootbbs.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import net.musecom.bootbbs.entity.Board;
import net.musecom.bootbbs.repository.BoardRepository;

@Service
public class BoardService {

   private final BoardRepository boardRepository;

   public BoardService(BoardRepository boardRepository) {
      this.boardRepository = boardRepository;
   }

   // 글조회
   public Page<Board> findAll(Pageable pageable) {
      return boardRepository.findAll(pageable);
   }

   // 검색
   public Page<Board> search(String key, String val, Pageable pageable) {
      switch (key) {
         case "writer":
            return boardRepository.findByWriterContaining(val, pageable);

         case "title":
            return boardRepository.findByTitleContaining(val, pageable);
         case "content":
            return boardRepository.findByContentContaining(val, pageable);
         default:
            return boardRepository.findAll(pageable);
      }
   }

   // 글 상세조회 Optional<T> 값이 있을수도 없을수도 있을때 명시적으로 쓰는 클래스
   public Optional<Board> findById(long id) {
      return boardRepository.findById(id);
   }

   // 글 등록, 글 수정
   public Board save(Board board) {
      return boardRepository.save(board);
   }

   // 글 삭제
   public void deleteById(long id) {
      boardRepository.deleteById(id);
   }
}
