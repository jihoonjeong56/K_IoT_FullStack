package org.example.demo_ssr_v1._core.config;


import lombok.RequiredArgsConstructor;
import org.example.demo_ssr_v1._core.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring MVC 설정 클래스
 *
 * @C, @S, @R, @Component
 */
//@Component 클래스 내부에서 @Bean 어노테이션을 사용해야 된다면 @Configuration 사용해야함
@Configuration // 내부도 IoC 대상 여부 확인
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
    // DI 처리 (생성자 의존 주입 받음)

    private final LoginInterceptor loginInterceptor;

    // ps. 인터셉터는 당연히 여러개 등록 가능
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 1. 설정에 LoginInterceptor를 등록하는 코드
        // 2. 인터셉터가 동작할 URL 패턴 지정
        // 3. 어떤 URL 요청이 로그인 여부를 필요할지 확인해야 함.
        //  /board/**
        //  /user/**
        //  --> 단 특정 URL은 제외 시킬거임
        registry.addInterceptor(loginInterceptor).addPathPatterns("/board/**", "/user/**")
                .excludePathPatterns("/login", "/join", "/logout", "/board/list", "/", "/board/{id:\\d+}",
                        "/css/**", "/js/**", "/images/**",
                        "/favicon.io", "/h2-console/**"
                );
        // \\d+는 정규표현식으로 1개 이상의 숫자를 의미함
    }
}
