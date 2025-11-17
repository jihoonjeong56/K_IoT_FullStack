import type { ApiResponse } from "@/types/ApiResponse";
import { API_ROUTES } from "./common/apiMappingPattern";
import { publicApi } from "./common/axiosInstance";
import type { UserDetail } from "@/types/user.type";

export async function fetchUserById(userId: number): Promise<UserDetail> {
  const res = await publicApi.get<ApiResponse<UserDetail>>(
    API_ROUTES.USERS.DETAIL(userId)
  );
  return res.data.data;
}
