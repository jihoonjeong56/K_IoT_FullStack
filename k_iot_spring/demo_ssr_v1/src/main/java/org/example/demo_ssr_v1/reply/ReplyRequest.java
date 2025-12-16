package org.example.demo_ssr_v1.reply;

import lombok.Data;
import org.example.demo_ssr_v1._core.errors.exception.Exception400;

public class ReplyRequest {

    @Data
    private static class UpdateDTO{
        private String comment;
        private String username;

        public void update(String newString) {
            if(newString == null || newString.trim().isEmpty()){
                throw new Exception400("댓글내용은 필수 입니다.");
            }
            if(newString.length()> 500){
                throw new Exception400("댓글은 500자 이하여야 합니다.");
            }
        }
    }
}
