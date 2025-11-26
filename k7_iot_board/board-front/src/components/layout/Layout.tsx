import React, { useState } from "react";
import { css } from "@emotion/react";
import Header from "./Header";
import Sidebar from "./Sidebar";
import Footer from "./Footer";

function Layout({ children }: { children: React.ReactNode }) {
  const [sidebarOpen, setSidebarOpen] = useState<boolean>(false);

  const handleToggleSidebar = () => {
    setSidebarOpen((prev) => !prev);
  };
  const handleClosedSidebar = () => {
    setSidebarOpen(false);
  };

  return (
    <div css={layoutStyle}>
      <Header onToggleSidebar={handleToggleSidebar} />
      <div css={contentStyle}>
        <Sidebar isOpen={sidebarOpen} onClose={handleClosedSidebar} />
        <main css={mainStyle}>{children}</main>
      </div>
      <Footer />
    </div>
  );
}

export default Layout;

const layoutStyle = css`
  /* display: grid; */
  display: flex;
  height: 100vh;
  flex-direction: column;

  /* grid-template-columns: minmax(150px, var(--sidebar-width)); */

  /* 행 높이 설정: 헤더[높이 값(고정)], 메인[가면(남은길이)], 푸터[높이값(고정)] */
  grid-template-rows: var(--header-height) 1fr var(--footer-height);
  /* 열 너비 설정: 사이드바[너비값(고정)], 메인[가변길이(남은길기)]  */
  grid-template-columns: var(--sidebar-width) 1fr;

  height: 100%;

  /* 위의 template 행과 열의 공간을 어떤 html 코드가 사용할 것인지 특정 */
  grid-template-areas:
    "header header"
    "sidebar main"
    "footer footer";

  /* 폭이 좁아지면 grid가 자동 재배치 */
  > header {
    grid-area: header;
  }
  > aside {
    grid-area: sidebar;
  }
  > main {
    grid-area: main;
    overflow-y: auto;
    padding: 1rem;
  }
  > footer {
    grid-area: footer;
  }
`;
const contentStyle = css`
  flex: 1;

  display: flex;
  overflow: hidden;

  transition: all 0.25 ease;
`;
const mainStyle = css`
  flex: 1;
  padding: 1.5rem;
  overflow-y: auto;

  display: flex;
  flex-direction: column;

  gap: 1rem;
`;
