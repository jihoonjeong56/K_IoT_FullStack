package org.example.demo_ssr_v1.reply;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class ReplyController {
    private final ReplyService replyService;
}
