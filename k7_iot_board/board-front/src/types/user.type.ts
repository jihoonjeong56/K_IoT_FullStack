// user.type.ts 사용자 관련 타입 정의

// 1) 도메일(Table, Entity  기준)
//    + Omit, Partial 방법
// 2) 도메인
//    + DTO 타입 정의 분리
export interface User {}

export interface UserDetail {}

export interface UserListData {}

export type UserList = UserListData[];

// users.type.ts 파일로 분리해야 할 코드
export interface UserDetail {}
