package org.example.demo_ssr_v1.refund;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.demo_ssr_v1._core.errors.exception.Exception401;
import org.example.demo_ssr_v1.payment.Payment;
import org.example.demo_ssr_v1.payment.PaymentResponse;
import org.example.demo_ssr_v1.user.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class RefundController {

    private final RefundService refundService;

    // 사용자 단 - > 환불 요청 화면 (검증 코드)
    @GetMapping("/refund/request/{paymentId}")
    public String refundRequestForm(@PathVariable Long paymentId,
                                    Model model, HttpSession session) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        Payment payment  = refundService.환불요청폼화면검증(paymentId, sessionUser.getId());
        PaymentResponse.ListDTO paymentDTO = new PaymentResponse.ListDTO(payment);
        model.addAttribute("payment", paymentDTO);
        return "refund/request-form";
    }

    @PostMapping("/refund/request")
    @ResponseBody
    public String refundRequest(@RequestBody RefundRequest.RequestDTO req, HttpSession session){
        User sessionUser = (User) session.getAttribute("sessionUser");
        if(sessionUser == null){
            throw new Exception401("로그인 필요");
        }
        req.validate();
        refundService.환불요청(sessionUser.getId(), req );
        return "redirect:/refund/list";
    }

}
