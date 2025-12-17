package org.example.demo_ssr_v1.reply;


import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.demo_ssr_v1.user.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class ReplyController {
    private final ReplyService replyService;

    /**
     * 댓글 작성 기능 요청
     * @param saveDTO
     * @param session
     * @return
     */
    @PostMapping("/reply/save")
    public String saveProc(ReplyRequest.SaveDTO saveDTO, HttpSession session) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        // 1. 인증 검사()-> 로그인 인터셉터가 인증 검사함 - > /reply/**
        // 2. 유효성 검사(형식 검사)
        saveDTO.validate();
        // 3. 댓글 작성 요청(서비스)
        replyService.댓글작성(saveDTO, sessionUser.getId());
        return "redirect:/board/" + saveDTO.getBoardId();
    }

    @PostMapping("/reply/{replyId}/delete")
    public String deleteProc(@PathVariable Long replyId, HttpSession session){
        User sessionUser = (User) session.getAttribute("sessionUser");
        Long boardId = replyService.댓글삭제(replyId, sessionUser.getId());
        // 댓글 삭제 후 게시글 상세보기 리다이렉트 처리

        return "redirect:/board/" + boardId;

    }

}
