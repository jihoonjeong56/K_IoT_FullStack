package org.example.demo_ssr_v1.reply;


import lombok.Data;
import org.example.demo_ssr_v1._core.utils.MyDateUtil;

public class ReplyResponse {
    @Data
    public static class ListDTO{
        private Long id;
        private String comment;
        private Long userId;
        private String username;
        private String createdAt;
        private boolean isOwner;

        public ListDTO(Reply reply, Long sessionUserId){
            this.id = reply.getId();
            this.comment = reply.getComment();
            if(reply.getUser() != null){
                this.userId = reply.getUser().getId();
                this.username = reply.getUser().getUsername();
            }
            if (reply.getCreatedAt() != null) {

                this.createdAt = MyDateUtil.timestampFormat(reply.getCreatedAt());
            }
            this.isOwner = reply.isOwner(sessionUserId);
        }
    }
}
