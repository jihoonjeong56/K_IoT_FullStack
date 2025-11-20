// board.api.ts

import type { ApiResponse } from "@/types/common/ApiResponse";
import { publicApi } from "../common/axiosInstance";
import { BOARD_FILE_PATH } from "./board.file.path";
import type { BoardListResponse } from "@/types/board/board.dto";

export const boardApi = {
  uploadBoardFiles: async (boardId: number, formData: FormData) => {
    const res = await publicApi.post<void>(
      BOARD_FILE_PATH.FILES_BY_BOARD(boardId),
      formData,
      { headers: { "Content-Type": "multipart/form-data" } }
    );
    return res.data;
  },
  getFilesByBoard: async (boardId: number) => {
    const res = await publicApi.get<BoardListResponse>(
      BOARD_FILE_PATH.FILES_BY_BOARD(boardId)
    );
    console.log("data: ", res.data);
    return res.data;
  },
  DOWNLOAD: async (fileId: number): Promise<Blob> => {
    const res = await publicApi.get<Blob>(BOARD_FILE_PATH.DOWNLOAD(fileId), {
      responseType: "blob",
    });
    return res.data;
  },
  DELETE_BOARD_FILE: async (fileId: number): Promise<void> => {
    await publicApi.delete(BOARD_FILE_PATH.FILES_BY_ID(fileId));
  },
  UPDATE_BOARD_FILES: async (boardId: number, formData: FormData) => {
    const res = await publicApi.put<void>(
      BOARD_FILE_PATH.FILES_BY_BOARD(boardId),
      formData,
      { headers: { "Content-Type": "multipart/form-data" } }
    );
    return res.data;
  },
};
