package com.fourbao.bookbao.backend.common.security;

import com.fourbao.bookbao.backend.filter.JwtAuthorizationFilter;
import com.fourbao.bookbao.backend.repository.UserRepository;
import com.fourbao.bookbao.backend.service.BookBaoPrincipalService;
import com.fourbao.bookbao.backend.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final BookBaoPrincipalService bookBaoPrincipalService;

    // spring security 보안 규칙 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )
                .userDetailsService(bookBaoPrincipalService)
                .addFilter(this.corsFilter())
                // jwt filter
                .addFilterBefore(
                        new JwtAuthorizationFilter(userRepository, jwtUtils),
                        BasicAuthenticationFilter.class
                )
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests.anyRequest().authenticated()
                )
                .logout(logout ->
                        logout.logoutSuccessUrl("/")
                                .deleteCookies("JSESSIONID")
                );

        return httpSecurity.build();
    }

    // NON_AUTHENTICATED에 담긴 패턴은 ignore
    @Bean
    public WebSecurityCustomizer securityCustomizer() {
        final String[] NON_AUTHENTICATED = {
                "/api/v1/**"
        };
        return web -> {
            web.ignoring().requestMatchers(NON_AUTHENTICATED);
        };
    }


    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*"); // e.g. http://domain1.com
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.addExposedHeader("Authorization");
        config.addExposedHeader("Set-Cookie");
        config.addExposedHeader("Cookie");
        config.addExposedHeader("X-Session-ID");

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

}