package org.example.demo_ssr_v1.reply;

import lombok.RequiredArgsConstructor;
import org.example.demo_ssr_v1._core.errors.exception.Exception403;
import org.example.demo_ssr_v1._core.errors.exception.Exception404;
import org.example.demo_ssr_v1.board.Board;
import org.example.demo_ssr_v1.board.BoardRepository;
import org.example.demo_ssr_v1.user.User;
import org.example.demo_ssr_v1.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReplyService {
    private final ReplyRepository replyRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    // 댓글 작성
    @Transactional
    public Reply 댓글작성(ReplyRequest.SaveDTO saveDTO, Long sessionUserId){
        // 1. 게시글 존재 유무 확인
        // 2. 현재 로그인 여부 확인
        // 3. 인가처리 필요 없음
        // 4. 요청 DTO를 엔티티로 변환 처리
        // 5. 저장 요청

        // 조회 했기 때문에 board 는 영속화 상태
        Board boardEntity = boardRepository.findById(saveDTO.getBoardId())
                .orElseThrow(() -> new Exception404("게시글 찾을 수 없음"));
        // 영속화 상태
        User userEntity = userRepository.findById(sessionUserId)
                .orElseThrow(() -> new Exception404("사용자 찾을 수 없음"));

        // 비 영속
        Reply reply = saveDTO.toEntity(boardEntity, userEntity);

        return replyRepository.save(reply);
    }


    // 댓글 목록 조회
    /**
     * OSIV 대응하기 위해 DTO 설계, 계층간 결합도를 줄이기 위해 설계
     * - JOIN FETCH로 한번에 User를 들고옴.
     * @param boardId
     * @param sessionUserId
     * @return
     */

    public List<ReplyResponse.ListDTO> 댓글목록조회(Long boardId, Long sessionUserId){
        // 1. 조회 -- List<Reply>
        // 2. 인가 처리 안함
        // 3. 데이터변환(DTO 생성) List<ReplyResponse.ListDTO>

        // 조회를 했기 때문에 1차 캐시에 들어간 상태 -> 즉 영속화 되어 있음
        List<Reply> replyList = replyRepository.findByBoardIdWithUser(boardId);

        return  replyList.stream().map(reply -> new ReplyResponse.ListDTO(reply, sessionUserId))
                .toList();
    }

    // 댓글 삭제
    @Transactional
    public Long 댓글삭제(Long replyId, Long sessionUserId){
        // 댓글 조회 (findById(reply) --> LAZY 때문에 댓글 작성자 정보는 없
        // 소유자 확인 해야 하기 때문에 댓글 작성자 정보 함께 필요
        // 권한 체크
        // 댓글 삭제 처리
        Reply replyEntity = replyRepository.findByIdWithUser(replyId)
                .orElseThrow(() -> new Exception404("댓글이 존재하지 않습니다."));

        if(!replyEntity.isOwner(sessionUserId)){
            throw new Exception403("권한 없음");
        }
        Long boardId = replyEntity.getBoard().getId();
        replyRepository.delete(replyEntity);
        // 컨트롤러에서 리다이랙트 처리해서 다시 게시글 상세보기 호출하기 위함 board id 값
        return boardId;
    }


}
