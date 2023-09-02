package com.foocmend.configs;

import com.foocmend.commons.validators.security.LoginFailureHandler;
import com.foocmend.commons.validators.security.LoginSuccessHandler;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.formLogin(f -> {
                    f.loginPage("/member/login")
                    .usernameParameter("email")
                    .passwordParameter("password")
                    .successHandler(new LoginSuccessHandler())
                    .failureHandler(new LoginFailureHandler());

        });

        http.logout(f -> {
                        f.logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
                        .logoutSuccessUrl("/member/login");

        });

        http.headers(f -> {
            f.frameOptions(o -> o.sameOrigin());
        });

        http.authorizeHttpRequests(c -> {
                    c.requestMatchers("/mypage/**").authenticated() // 회원전용
                  // .requestMatchers("/admin/**").hasAuthority("ADMIN") // 관리자 전용
                   .anyRequest().permitAll(); // 그외 모든 페이지 접근 가능
        });

        http.exceptionHandling(c -> {
            c.authenticationEntryPoint((req, res, e) -> {
                String URI = req.getRequestURI();

                if (URI.indexOf("/admin") != -1) { // 관리자 페이지
                    res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "NOT AUTHORIZED");
                } else { // 회원 전용 페이지
                    String redirectURL = req.getContextPath() + "/member/login";
                    res.sendRedirect(redirectURL);
                }
            });
        });

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return w -> w.ignoring().requestMatchers(
                "/front/css/**",
                "/front/js/**",
                "/mobile/css/**",
                "/mobile/js/**",
                "/admin/css/**",
                "/admin/js/**",
                "/common/css/**",
                "/common/js/**",
                "/images/**",
                "/errors/**");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
