package com.fourbao.bookbao.backend.common.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable())
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )
                .formLogin(form -> form.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .addFilter(this.corsFilter())
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests.anyRequest().authenticated()
                )
                .logout(logout ->
                        logout.logoutSuccessUrl("/")
                                .deleteCookies("JSESSIONID")
                );

        return httpSecurity.build();
    }

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
        config.addAllowedOrigin("http://localhost:3000");       // todo
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