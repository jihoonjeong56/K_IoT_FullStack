package org.example.demo_ssr_v1.board;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


import java.util.List;

@RequiredArgsConstructor // DI
@Controller // IoC
public class BoardController {

    private final BoardPersistRepository repository;

    // 게시글 수정 폼 페이지 요청
    // http://localhost:8080/board/1/update
    @GetMapping("/board/{id}/update")
    public String updateForm(@PathVariable Long id, Model model) {

        Board board = repository.findById(id);
        if (board == null) {
            throw new RuntimeException("수정할 게시글을 찾을 수 없음");
        }
//        HttpServletRequest request
//                -> request.setAttribute("board", board);
        model.addAttribute("board", board);


        return "board/update-form";
    }

    // 게시글 수정 요청(기능요청)
    // http://localhost:8080/board/1/update
    @PostMapping("/board/{id}/update")
    public String updateProc(@PathVariable Long id, BoardRequest.UpdateDTO updateDTO) {
        try {
            repository.updateById(id, updateDTO);
            // 더티체킹 전략
        } catch (Exception e) {
            throw new RuntimeException(" 게시글 수정 실패");
        }


        return "redirect:/board/list";
    }

    // http://localhost:8080/board/list
    @GetMapping({"/board/list", "/"})
    public String boardList(Model model) {
        List<Board> boardList = repository.findAll();
        model.addAttribute("boardList", boardList);
        return "board/list";
    }

    // http://localhost:8080/board/save
    // 게시글 저장 화명 요청
    @GetMapping("/board/save")
    public String saveForm() {
        return "board/save-form";
    }

    // http://localhost:8080/board/save
    // 게시글 저장 기능요청
    @PostMapping("/board/save")
    public String saveProc(BoardRequest.SaveDTO saveDTO) {
        // HTTP 요청: username = 값&title=값&content=값
        Board board = saveDTO.toEntity();

        repository.save(board);
        return "redirect:/";
    }

    @GetMapping("/board/{id}")
    public String boardDetail(@PathVariable Long id, Model model) {
        Board board = repository.findById(id);
        if (board == null) {
            throw new RuntimeException("해당 id를 찾을 수 없습니다." + id);
        }
        model.addAttribute(board);
        return "board/detail";
    }

    // 삭제
    @PostMapping("/board/{id}/delete")
    public String delete(@PathVariable Long id) {
        repository.deleteById(id);

        return "redirect:/";
    }


}
