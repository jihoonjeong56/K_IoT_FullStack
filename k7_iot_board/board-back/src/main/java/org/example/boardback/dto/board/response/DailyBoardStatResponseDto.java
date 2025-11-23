package org.example.boardback.dto.board.response;

import java.time.LocalDateTime;
import java.util.Map;


/**
 * 통계 응답 타입들 (간단한 형태)
 */
public record DailyBoardStatResponseDto(
        Map<LocalDateTime, Long> dateToCount
) {
}
