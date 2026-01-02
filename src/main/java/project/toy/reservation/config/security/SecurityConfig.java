package project.toy.reservation.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    @Value("${security.remember-me.key}")
    private String rememberMeKey;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // CSRF는 개발 단계에서는 비활성화 (템플릿 없어서 테스트용)
                .csrf(csrf -> csrf.disable())

                // URL 권한 설정
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/main", "/member/signup", "/member/login").permitAll()
                        .requestMatchers("/images/**", "slide/**").permitAll() // 정적 리소스 허용
                        .anyRequest().authenticated()
                )

                // 세션 기반 로그인 폼 설정
                .formLogin(form -> form
                        .loginPage("/member/login")             // 로그인 페이지 URL
                        .loginProcessingUrl("/member/login")   // 로그인 form POST URL
                        .usernameParameter("email")
                        .defaultSuccessUrl("/main")  // 로그인 성공 후 이동
                        .failureUrl("/member/login?error=true")// 로그인 실패 시
                        .permitAll()
                )

                // 로그아웃 설정
                .logout(logout -> logout
                        .logoutUrl("/member/logout")
                        .logoutSuccessUrl("/main")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID", "remember-me")
                        .permitAll()
                )

                // Remember-Me 설정
                .rememberMe(rem -> rem
                        .key(rememberMeKey)                   // 쿠키 서명용 key
                        .tokenValiditySeconds(60 * 60 * 24 * 7)  // 7일 동안 유지
                        .userDetailsService(userDetailsService)
                );

        return http.build();
    }

    // 비밀번호 암호화
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
