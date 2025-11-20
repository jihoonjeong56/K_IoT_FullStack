// board.dto.ts

export interface BoardFileListDto {
  fileId: number;
  originalName: string;
  storedNma: string;
  contentType: string;
  fileSize: number;
  downloadUrl: string;
}
export type BoardListResponse = BoardFileListDto[];

export interface BoardFileUpdateReq {
  keepFileids?: number[];
  files?: File[];
}
