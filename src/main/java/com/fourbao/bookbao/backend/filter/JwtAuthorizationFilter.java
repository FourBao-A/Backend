package com.fourbao.bookbao.backend.filter;

import com.fourbao.bookbao.backend.common.exception.BaseException;
import com.fourbao.bookbao.backend.common.response.BaseResponseStatus;
import com.fourbao.bookbao.backend.dto.security.BookBaoPrincipal;
import com.fourbao.bookbao.backend.entity.User;
import com.fourbao.bookbao.backend.repository.UserRepository;
import com.fourbao.bookbao.backend.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


/**
 * 인가 관련 구현
 * - Authorization 헤더의 bearer 토큰 확인
 * - token 으로부터 User의 schoolId 추출
 * - schoolId로 Database 검색해 유저에 관한 정보를 SpringSecurityContextHolder에 넣음
 */

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        log.info("[JwtAuthorizationFilter] Authorization start");
        String jwtHeader = request.getHeader("Authorization");

        // header 유무 확인
        if (jwtHeader == null || !jwtHeader.startsWith("Bearer")) {
            chain.doFilter(request, response);
            return;
        }

        String jwtToken = jwtHeader.replace("Bearer ", "");
        String userSchoolId = jwtUtils.getUserSchoolId(jwtToken);
        log.info("[JwtAuthorizationFilter] Valid Token");

        if (userSchoolId != null) {
            User user = userRepository.findBySchoolId(userSchoolId)
                    .orElseThrow(() -> new BaseException(BaseResponseStatus.NON_EXIST_USER));

            BookBaoPrincipal bookBaoPrincipal = BookBaoPrincipal.createBookbaoPrincipalByUserEntity(user);

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    bookBaoPrincipal,
                    null,
                    bookBaoPrincipal.getAuthorities()
            );
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("[JwtAuthorizationFilter] Authentication Set Successful");
            chain.doFilter(request, response);
        }
    }
}
