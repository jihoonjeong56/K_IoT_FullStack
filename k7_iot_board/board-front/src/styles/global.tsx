//! global.ts

import { css, Global } from "@emotion/react";

export const GlobalStyle = () => (
  <Global
    styles={css`
      /* 
      ! * 전체 선택자는 모든 요소를 가리킴 
      > before, after - 해당 속성의 "가상요소" 는 해당 * 전체 선택자 속성에 포함되지 않음
      */
      *,
      *::before,
      *::after {
        margin: 0;
        padding: 0;
        box-sizing: border-box;
      }

      html,
      body,
      #root {
        height: 100%;
      }

      body {
        /*
        ! font-family: ;
         */
        background: #f8fafc;
        color: #111827;
      }
      a {
        text-decoration: none;
      }
      ul {
        list-style: none;
      }
      :root {
        --primary: #4f46e5;
        --sidebar-width: 220px;
        --footer-height: 50px;
        --header-height: 50px;
      }
    `}
  />
);
