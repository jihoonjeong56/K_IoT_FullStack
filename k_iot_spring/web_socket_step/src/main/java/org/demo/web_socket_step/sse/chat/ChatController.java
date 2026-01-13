package org.demo.web_socket_step.sse.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    // 1. 채팅방 화면(기존 글 목록 + SSE 연결 JS 포함)
    @GetMapping("/sse/chat")
    public String index(Model model) {

        return "sse/index";
    }

    //produces: 나는 이런 종류 데이터를 생산한다.
    // MediaType.TEXT_HTML_VALUE - html 파일 평식
    // MediaType.APPLICATION_JSON_VALUE - 데이터 (JSON 형식)

    // 2. SSE 연결 여기 경로로 오면 클라이언트 이제 구독함
    // HTTP 메세지 헤더에 이제부터 지속 연결 명시
    @GetMapping(value = "/sse/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseBody
    public SseEmitter connect() {
        // SseEmitter 객체를 설정하고 준비해서 js 측으로 반환 처리
        // 서비스 탄에서 처리
        return chatService.createConnection(UUID.randomUUID().toString());
    }

    // 메시지 전송
    @PostMapping("/sse/send")
    public String sendMessage(@RequestParam(name = "message") String message,
                              @RequestParam(name = "sender") String sender) {
        chatService.addMessage(message, sender);
        return "redirect:/sse/chat";
    }
}
