import type { ApiResponse } from "@/types/common/ApiResponse";
import { privateApi, publicApi } from "../common/axiosInstance";
import type { UserDetailResDto, UserListResponse } from "@/types/user/user.dto";
import { USER_PATH } from "./user.path";

export const userApi = {
  getUser: async (userId: number): Promise<UserDetailResDto> => {
    const res = await privateApi.get<ApiResponse<UserDetailResDto>>(
      USER_PATH.BY_ID(userId)
    );
    return res.data.data;
  },
  getUserList: async (): Promise<UserListResponse> => {
    const res = await publicApi.get<ApiResponse<UserListResponse>>(
      USER_PATH.LIST
    );
    return res.data.data;
  },
};

// export async function fetchUserById(userId: number): Promise<UserDetail> {
//   const res = await publicApi.get<ApiResponse<UserDetail>>(
//     API_ROUTES.USERS.DETAIL(userId)
//   );
//   return res.data.data;
// }
