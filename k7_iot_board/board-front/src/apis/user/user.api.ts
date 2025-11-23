// user.api.ts

import { privateApi } from "../common/axiosInstance";
import type {
  UserDetailResponse,
  UserListResponse,
} from "@/types/user/user.dto";
import { USER_PATH } from "./user.path";
import type { ResponseDto } from "@/types/common/ResponseDto";

export const userApi = {
  getUser: async (userId: number): Promise<UserDetailResponse> => {
    const res = await privateApi.get<ResponseDto<UserDetailResponse>>(
      USER_PATH.BY_ID(userId)
    );
    if (res.data.data) {
      return res.data.data;
    } else {
      throw new Error("해당 id의 user가 존재하지 않습니다.");
    }
  },

  getUserList: async (): Promise<UserListResponse> => {
    const res = await privateApi.get<ResponseDto<UserListResponse>>(
      USER_PATH.LIST
    );
    if (res.data.data) {
      return res.data.data;
    } else {
      throw new Error("사용자 리스트를 불러올 수 없습니다.");
    }
  },
};
