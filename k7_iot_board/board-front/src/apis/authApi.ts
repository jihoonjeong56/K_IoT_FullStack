import type { LoginRequest, LoginResponse } from "@/types/auth.type";
import { publicApi } from "./common/axiosInstance";
import { API_ROUTES } from "./common/apiMappingPattern";
import type { ApiResponse } from "@/types/ApiResponse";

// 로그인 요청
export async function login(req: LoginRequest): Promise<LoginResponse> {
  const res = await publicApi.post<ApiResponse<LoginResponse>>(
    API_ROUTES.AUTH.LOGIN,
    req
  );
  return res.data.data;
}
