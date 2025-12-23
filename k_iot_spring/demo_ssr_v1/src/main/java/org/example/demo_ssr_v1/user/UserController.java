package org.example.demo_ssr_v1.user;


import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

/**
 * 사용자 Controller (표현 계층)
 * 핵심 개념 :
 * - HTTP 요청을 받아서 처리
 * - 요청 데이터 검증 및 파마리터 바인딩
 * - Service 레이어에 비즈니스 로직을 위힘
 * - 응답 데이터를 View 에 전달 함
 */

@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserService userService;

    @Value("${oauth.kakao.client-id}")
    private String clientId;

    @Value("${tenco.key}")
    private String tencoKey;

    //TODO 테스트용 코드 - 삭제 예정
//    @PostConstruct
//    public void init(){
//        System.out.println("현재 적용된 카카오 클라이언트 키: " + clientId);
//        System.out.println("현재 적용된 나의 시크릿 키: " + tencoKey);
//    }

    // [흐름] 1. 인가코드 받기 -> 2. 토큰(JWT) 발급 요청 -> 3. JWT 으로 사용자 정보 요청 -> 4. 우리 서버에 로그인/회원가입 처리
    @GetMapping("/user/kakao")
//    @ResponseBody
    public String kakaoCallback(@RequestParam(name = "code") String code, HttpSession session) {

        // 1. 인가코드 확인
        System.out.println("1. 카카오 인가코드 확인: " + code);

        // 2. 토큰 발급 요청 해야함(https://kauth.kakao.com/oauth/token) -> POST
        // 2.1 HTTP 헤더 커스텀
        // Content-Type: application/x-www-form-urlencoded;charset=utf-8

        // 2.2
        RestTemplate restTemplate = new RestTemplate();

        // 2.3
        // HTTP 메싲지 헤더 구성
        HttpHeaders tokenHeaders = new HttpHeaders();
        tokenHeaders.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        // 2.4 HTTP 메시지 바디 구성(POST)
        MultiValueMap<String, String> tokenParams = new LinkedMultiValueMap<>();
        tokenParams.add("grant_type", "authorization_code");
        tokenParams.add("client_id", "73b2007155b6e1dbcc4b8a2f0e7e31ea");
        tokenParams.add("redirect_uri", "http://localhost:8080/user/kakao");
        tokenParams.add("code", code);
        tokenParams.add("client_secret", "z6DfROeC0r0Mde4VkQJtzPtSaNSl0QU0");

        // 2.5 바디 + 헤더 구성

        HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity<>(tokenParams, tokenHeaders);

        // 2.6 요청하고 JWT 토큰 응답 받기 (카카오에서 엑시ㅔ스 토큰이라고 부름)
        ResponseEntity<UserResponse.OAuthToken> tokenResponse = restTemplate.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST, tokenRequest, UserResponse.OAuthToken.class);

        // JWT 토큰 확인
        System.out.println("tokenHeader: " + tokenResponse.getHeaders());
        System.out.println("AccessToken: " + tokenResponse.getBody().getAccessToken());
        System.out.println("ExpiresIn: " + tokenResponse.getBody().getExpiresIn());


        // 3. 엑세스 토큰을 받았기 때문에 카카오 자원서버(User 정보..) 사용자에 대한 정보를 요청할 수 있다.
        // GET/POST  - > https://kapi.kakao.com/v2/user/me
        // 3.1 HTTP 클라이언트 선언
        RestTemplate profileRt = new RestTemplate();

        // 3.2 HTTP 메시지 헤더 커스텀
        HttpHeaders profileHeaders = new HttpHeaders();
        profileHeaders.add("Authorization", "Bearer " + tokenResponse.getBody().getAccessToken());
        profileHeaders.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        // 3.3 요청 메시지 생성 (요청 바디 없음 - 안만들어도 됨)
        HttpEntity<Void> profileRequest = new HttpEntity<>(profileHeaders);

        ResponseEntity<UserResponse.KakaoProfile> profileResponse = profileRt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST, profileRequest, UserResponse.KakaoProfile.class);

        // 3.4 사용자 정보 수신 완료
        System.out.println("Profile Id: " + profileResponse.getBody().getId());
        System.out.println("Profile Nickname: " + profileResponse.getBody().getProperties().getNickname());
        System.out.println("Profile ThumbnailImage: " + profileResponse.getBody().getProperties().getThumbnailImage());


        // 4. 최초 사용자라면 강제 회원가입 처리 및 로그인 처리
        // DB 에 회원가입 및 여부 확인 -> User 엔티티 수정
        // 소셜 로그인 닉네임과 기존 회원 가입 닉네임 중복 될 수 있음.
        UserResponse.KakaoProfile kakaoProfile = profileResponse.getBody();
        String username = kakaoProfile.getProperties().getNickname() + "_" + kakaoProfile.getId();
        User userOrigin = userService.사용자이름조회(username);
        if (userOrigin == null) {
            // 최소 카카오 소셜 로그인 사용자임
            System.out.println("기존 회원이 아님 -> 자동 회원가입 진행");
            User newUser = User.builder()
                    .username(username)
                    .password(tencoKey) // 소셜 로그인은 임시 비밀번호로 설증
                    .email(username + "@kakao.com") // 선택사항 (카카오 이메일 비즈니스 앱 신청)
                    .provider(OAuthProvider.KAKAO)
                    .build();
            // 프로필 이미지가 있다면 설정(카카오 사용자 정보에)
            String profileImage = kakaoProfile.getProperties().getProfileImage();
            if (profileImage != null && !profileImage.isEmpty()) {
                newUser.setProfileImage(profileImage); // 카카오에서 넘겨받은 URL 그대로 저장
            }
            userService.소셜회원가입(newUser);
            // 조심해야함 반드시 필요함
            userOrigin = newUser; // 반드시 넣어줘야함 -> 로그인 처리해야하기 때문

        } else {
            System.out.println("이미가입된 회원입니다. 바로 고르인 처리 진행합니다.");
        }
        session.setAttribute("sessionUser", userOrigin);

        return "redirect:/";
    }


    // 프로필 이미지 삭제 하기
    @PostMapping("/user/profile-image/delete")
    public String deleteProfileImage(HttpSession session) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        User updateUser = userService.프로필이미지삭제(sessionUser.getId());
        // 왜 user 다시 받을까? -- 세션 정보가 (즉 프로필이 삭제 되었기 때문에)
        // 세션 정보 갱신 처리 해주기 위함이다.
        session.setAttribute("sessionUser", updateUser); // 세션 정보 갱신

        // 일반적으로 POST 요청이 오면 PRG 패턴으로 설계 됨
        // POST -> Redirect 처리 ---> Get 요청
        return "redirect:/user/detail";
    }

    // 마이페이지
    // http://localhost:8080/user/detail
    @GetMapping("/user/detail")
    public String detail(Model model, HttpSession session) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        User user = userService.마이페이지(sessionUser.getId());

        model.addAttribute("user", user);
        return "user/detail";
    }


    // 회원 정보 수정 화면 요청
    // http://localhost:8080/user/update
    @GetMapping("/user/update")
    public String updateForm(Model model, HttpSession session) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        User user = userService.회원정보수정화면(sessionUser.getId());
        model.addAttribute("user", user);
        return "user/update-form";
    }


    // 회원 정수 수정 기능 요청 - 더티 체킹
    // http://localhost:8080/user/update
    @PostMapping("/user/update")
    public String updateProc(UserRequest.UpdateDTO updateDTO, HttpSession session) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        try {
            // 유효성 검사 (형식 검사)
            updateDTO.validate();
            User updateUser = userService.회원정보수정(updateDTO, sessionUser.getId());
            // 회원 정보 수정은 - 세션 갱신해 주어야 한다.
            session.setAttribute("sessionUser", updateUser);
            return "redirect:/user/detail";
        } catch (Exception e) {
            return "user/update-form";
        }
    }


    // 로그아웃 기능 요청
    // http://localhost:8080/logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // 세션 무효화
        session.invalidate();
        return "redirect:/";
    }

    // 로그인 화면 요청
    // http://localhost:8080/login
    @GetMapping("/login")
    public String loginForm() {
        return "user/login-form";
    }


    // http://localhost:8080/login
    @PostMapping("/login")
    public String loginProc(UserRequest.LoginDTO loginDTO, HttpSession session) {
        try {
            // 유효성 검사
            loginDTO.validate();
            User sessionUser = userService.로그인(loginDTO);
            session.setAttribute("sessionUser", sessionUser);
            return "redirect:/";
        } catch (Exception e) {
            // 로그인 실패시 다시 로그인 화면으로 처리
            return "user/login-form";
        }
    }


    // 회원가입 화면 요청
    // http://localhost:8080/join
    @GetMapping("/join")
    public String joinFrom() {
        return "user/join-form";
    }

    // 회원가입 기능 요청
    // http://localhost:8080/join
    @PostMapping("/join")
    public String joinProc(UserRequest.JoinDTO joinDTO) {
        joinDTO.validate();
        userService.회원가입(joinDTO);
        return "redirect:/login";
    }

}
