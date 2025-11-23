//! board.file.path.ts
// : 게시판 파일 관련 프론트엔드 호출용 Path 구조

import { BASE } from "../common/base.path";

const BOARD_FILE_PREFIX = `${BASE}/boards`;

export const BOARD_FILE_PATH = {
  //# 게시글 파일 컬렉션 (업로드, 목록, 수정)
  FILES_BY_BOARD: (boardId: number) => `${BOARD_FILE_PREFIX}/${boardId}/files`,

  //? 1. 파일 업로드: POST - /boards/{boardId}/files
  // : 게시글에 파일 추가

  //? 2. 파일 목록 조회: GET - /boards/{boardId}/files
  // : 해당 게시글의 첨부파일 목록 조회

  //? 3. 파일 다운로드: GET - /boards/files/{fileId}/download
  // : 개별 파일 다운로드
  DOWNLOAD: (fileId: number) => `${BOARD_FILE_PREFIX}/files/${fileId}/download`,

  //? 4. 파일 삭제: DELETE - /boards/files/{fileId}
  // : 개별 파일 삭제
  
  //# 개별 파일 리소스
  FILE_BY_ID: (fileId: number) => `${BOARD_FILE_PREFIX}/files/${fileId}`

  //^ cf) download와 delete는 boardId 없이 fileId 기준으로 직행하는 형태

  //? 5. 파일 수정(추가/삭제 혼합): PUT - /boards/{boardId}/files
  // : 게시글 첨부파일 목록 변경
}