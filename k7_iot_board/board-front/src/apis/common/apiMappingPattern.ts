// API 총정리

// 1) 단독 상수 사용
const AUTH_API = "/auth";
export const LOGIN = AUTH_API + "/login";

// 2) API 별 전용 파일 생성 - 모듈화 (routes.ts)
export const API_ROUTES = {
  AUTH: {
    LOGIN: "/auth/login",
    LOGOUT: "/auth/logout",
  },
  USERS: {
    DETAIL: (id: number) => `/users/${id}`,
    LIST: "/users",
  },
};
