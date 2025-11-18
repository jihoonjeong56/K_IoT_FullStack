import type { LoginRequest, LoginResponse } from "@/types/auth.type";
import { publicApi } from "../common/axiosInstance";
import type { ApiResponse } from "@/types/common/ApiResponse";
import { AUTH_PATH } from "./auth.path";

export const authApi = {
  // 로그인 요청
  login: async (req: LoginRequest): Promise<LoginResponse> => {
    // axios.메서드<반환타입>();
    const res = await publicApi.post<ApiResponse<LoginResponse>>(
      AUTH_PATH.LOGIN,
      req
    );
    return res.data.data;
  },
};

