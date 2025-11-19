package org.example.boardback.dto.board.file;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Setter
public class BoardFileUpdateRequestDto {
    private List<Long> keepFileIds; // 유지할 기존 파일 리스트
    private List<MultipartFile> newFiles; // 새로 추가되는 파일

}
