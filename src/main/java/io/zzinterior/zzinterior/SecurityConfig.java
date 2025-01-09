package io.zzinterior.zzinterior;

import io.zzinterior.zzinterior.entity.Role;
import io.zzinterior.zzinterior.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final TestService service;
    private final OAuth2SucessHandler oAuth2SucessHandler;
    private final TokenAuthenticationFilter tokenAuthenticationFilter;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){ //security를 적용하지 않을 리소스
        return web->web.ignoring()
                .requestMatchers("/error","/favicon.ico");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                // rest api 설정
                .csrf(AbstractHttpConfigurer::disable) // csrf 비활성화 -> cookie를 사용하지 않으면 꺼도 된다. (cookie를 사용할 경우 httpOnly(XSS 방어), sameSite(CSRF 방어)로 방어해야 한다.)
                .cors(AbstractHttpConfigurer::disable) // cors 비활성화 -> 프론트와 연결 시 따로 설정 필요
                .httpBasic(AbstractHttpConfigurer::disable) // 기본 인증 로그인 비활성화
                .formLogin(AbstractHttpConfigurer::disable) // 기본 login form 비활성화
                .logout(AbstractHttpConfigurer::disable) // 기본 logout 비활성화
                .headers(c -> c.frameOptions(
                        FrameOptionsConfig::disable).disable()) // X-Frame-Options 비활성화
                .sessionManagement(c ->
//                        c.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 사용하지 않음
                        c.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)) // 세션을 필요한 경우에만 생성

                // request 인증, 인가 설정
                .authorizeHttpRequests(request ->
                        request.requestMatchers(
                                new AntPathRequestMatcher("/"),
                                new AntPathRequestMatcher("/auth/success")
                                ).permitAll()
                                .anyRequest().authenticated()
                )

                // oauth2 설정
                .oauth2Login(oauth -> // OAuth2 로그인 기능에 대한 여러 설정의 진입점
                                    // OAuth2 로그인 성공 이후 사용자 정보를 가져올 때의 설정을 담당
                                    oauth.userInfoEndpoint(c -> c.userService(service))
                                    // 로그인 성공 시 핸들러
                                    .successHandler(oAuth2SucessHandler)
                )
                // jwt 관련 설정
                .addFilterBefore(tokenAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new TokenExceptionFilter(), tokenAuthenticationFilter.getClass()) // 토큰 예외 핸들링

                // 인증 예외 핸들링
                .exceptionHandling((exceptions) -> exceptions
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                        .accessDeniedHandler(new CustomAccessDeniedHandler()));

        return http.build();
    }
}
