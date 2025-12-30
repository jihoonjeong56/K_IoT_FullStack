package org.example.demo_ssr_v1.user;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;
    private final MailService mailService;


    @PostMapping("/api/email/send")
    public ResponseEntity<?> 인증번호발송(@RequestBody UserRequest.EmailCheckDTO reqDTO) {
        // 1. 유효성 검사
        reqDTO.validate();

        // 2. 서비스 단에서 구글 메일 서버로 이메일 전송 처리
        mailService.인증번호발송(reqDTO.getEmail());

        return ResponseEntity.ok().body(Map.of("message", "인증번호가 발송되었습니다."));
    }

    @PostMapping("/api/email/verify")
    public ResponseEntity<?> 인증번호확인(@RequestBody UserRequest.EmailCheckDTO reqDTO) {
        reqDTO.validate();

        //인증번호 입력확인
        if (reqDTO.getCode() == null || reqDTO.getCode().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "인증번호를 입력해 주세요"));
        }

        // 메일 서비스단에서 인증번호 확인
        boolean isVerified = mailService.인증번호확인(reqDTO.getEmail(), reqDTO.getCode());

        // 결과 값에 따라 분기처리
        if (isVerified) {
            // 인증성공
            return ResponseEntity.ok().body(Map.of("message", "인증이 완료되었습니다."));
        } else {
            return ResponseEntity.badRequest().body(Map.of("message", "인증이 실패하였습니다."));
        }

    }


    // api/point/charge
    @PostMapping("/api/point/charge")
    public ResponseEntity<?> chargePoint(@RequestBody UserRequest.PointChargeDTO pointChargeDTO, HttpSession session) {
        pointChargeDTO.validate();
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return ResponseEntity.status(401).body(Map.of("message", "로그인이 필요합니다."));
        }

        // 포인트 충전
        User updateUser = userService.포인트충전(sessionUser.getId(), pointChargeDTO.getAmount());

        // 세션에 업데이트 된 사용자 정보 갱신(포인트)
        session.setAttribute("sessionUser", updateUser);

        return ResponseEntity.ok().body(Map.of(
                "message", "포인트가 충전 되었습니다.",
                "amount", pointChargeDTO.getAmount(),
                "currentPoint", updateUser.getPoint()));

    }
}
