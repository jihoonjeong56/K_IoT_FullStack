// 공통 Response 형식
export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
}
