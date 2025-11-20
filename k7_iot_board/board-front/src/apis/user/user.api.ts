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
    return res.data.data;
  },

  getUserList: async (): Promise<UserListResponse> => {
    const res = await privateApi.get<ResponseDto<UserListResponse>>(
      USER_PATH.LIST
    );
    return res.data.data;
  },
};
