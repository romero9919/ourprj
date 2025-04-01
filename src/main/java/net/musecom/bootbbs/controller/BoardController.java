package net.musecom.bootbbs.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import net.musecom.bootbbs.entity.Board;
import net.musecom.bootbbs.service.BoardService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class BoardController {

  private final BoardService service;

  public BoardController(BoardService service) {
    this.service = service;
  }

  // 게시물 조회
  @GetMapping("/")
  public String list(
      @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
      @RequestParam(value = "searchKey", required = false) String searchKey,
      @RequestParam(value = "searchVal", required = false) String searchVal,
      Model model) {
    Page<Board> lists;
    if (searchKey != null && searchVal != null && !searchVal.isEmpty()) {
      lists = service.search(searchKey, searchVal, pageable);
    } else {
      lists = service.findAll(pageable);
    }
    model.addAttribute("lists", lists);
    model.addAttribute("searchKey", searchKey);
    model.addAttribute("searchVal", searchVal);
    return "list";
  }

  // 글 상세보기
  @GetMapping("/view")
  public String view(@ModelAttribute("id") long id, Model model) {
    Optional<Board> board = service.findById(id);
    if (board.isPresent()) {
      board.get().setHit(board.get().getHit() + 1);
      service.save(board.get());
      board.get().setContent(Jsoup.clean(board.get().getContent(), Safelist.relaxed()));
      model.addAttribute("board", board.get());
      return "view";
    } else {
      return "redirect:/";
    }
  }

  // 글쓰기
  @GetMapping("/write")
  public String writeForm(Model model) {
    model.addAttribute("fileKey", System.currentTimeMillis());
    return "write";
  }

  // 글등록
  @PostMapping("/write")
  public String write(@ModelAttribute Board board) {
    service.save(board);
    return "redirect:/";
  }

  // 글수정
  @GetMapping("/edit")
  public String editForm(@ModelAttribute("id") long id, Model model) {
    Optional<Board> board = service.findById(id);
    if (board.isPresent()) {
      board.get().setContent(Jsoup.clean(board.get().getContent(), Safelist.relaxed()));
      model.addAttribute("board", board.get());
      return "edit";
    } else {
      return "redirect:/";
    }
  }

  @PostMapping("/edit")
  public String edit(@ModelAttribute Board board, Model model) {
    Optional<Board> bbs = service.findById(board.getId());
    if (bbs.isPresent()) {
      Board obbs = bbs.get();
      if (obbs.getPass() != null && obbs.getPass().equals(board.getPass())) {
        // 나머지는 bbs의 값으로 하고 board로 받은 값(이름, 제목, 내용)만 업데이트
        obbs.setWriter(board.getWriter());
        obbs.setTitle(board.getTitle());
        obbs.setContent(board.getContent());
        service.save(obbs);
        return "redirect:/view?id=" + board.getId();
      } else {
        model.addAttribute("error", "비밀번호가 일치하지 않습니다.");
        model.addAttribute("board", obbs);
        return "edit";
      }
    }
    return "redirect:/";
  }

  // 글삭제
  @PostMapping("/delete")
  @ResponseBody
  public String delete(@RequestBody Map<String, Object> param) {
    long id = Long.parseLong(param.get("id").toString());
    String pass = param.get("pass").toString();

    Optional<Board> boardOp = service.findById(id);
    if (boardOp.isPresent()) {
      Board board = boardOp.get();
      // 비밀번호 비교
      if (board.getPass() != null && board.getPass().equals(pass)) {
        service.deleteById(id);
        return "success";
      } else {
        return "fail";
      }
    } else {
      return "fail";
    }
  }

}
