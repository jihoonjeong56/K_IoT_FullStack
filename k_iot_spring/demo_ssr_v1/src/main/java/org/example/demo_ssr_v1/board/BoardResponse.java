package org.example.demo_ssr_v1.board;

import lombok.Data;
import org.example.demo_ssr_v1._core.utils.MyDateUtil;
import org.example.demo_ssr_v1.user.User;

public class BoardResponse {

    /**
     * 게시글 목록 응답 DTO
     */
    @Data
    public static class ListDTO {
        private Long id;
        private String title;
        private String username;
        private String createdAt;

        public ListDTO(Board board) {
            this.id = board.getId();
            this.title = board.getTitle();
            // 쿼리 --> JOIN FETCH 로 가져오면 문제 없음
            if (board.getUser().getUsername() != null) {
                this.username = board.getUser().getUsername();
            }
            if (board.getCreatedAt() != null) {
                this.createdAt = MyDateUtil.timestampFormat(board.getCreatedAt());
            }
        }
    } // end of static inner class


    /**
     * 게시글 상세 응답 DTO
     */
    @Data
    public static class DetailDTO {
        private Long id;
        private String title;
        private String content;
        private Long userId;
        private String username;
        private String createdAt;

        public DetailDTO(Board board) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
            // JOIN FETCH 활용 (한번에 JOIN 에서 Repository 에서 가지고 올 예정)
            if (board.getUser() != null) {
                this.userId = board.getUser().getId();
                this.username = board.getUser().getUsername();
            }
            if (board.getCreatedAt() != null) {
                this.createdAt = MyDateUtil.timestampFormat(board.getCreatedAt());
            }
        }
    } // end of class

    /**
     * 게시글 수정 화면 응답 DTO
     */
    @Data
    public static class UpdateFormDTO{
        private Long id;
        private String title;
        private String content;
        private String username;

        public UpdateFormDTO(Board board) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
            this.username = board.getUser().getUsername();
        }
    }

}
