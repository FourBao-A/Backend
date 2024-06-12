package com.fourbao.bookbao.backend.service;

import com.fourbao.bookbao.backend.common.exception.BaseException;
import com.fourbao.bookbao.backend.common.response.BaseResponseStatus;
import com.fourbao.bookbao.backend.dto.request.UserLoginRequest;
import com.fourbao.bookbao.backend.dto.response.PortalApiResponse;
import com.fourbao.bookbao.backend.entity.User;
import com.fourbao.bookbao.backend.repository.UserRepository;
import com.fourbao.bookbao.backend.utils.JwtUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.fourbao.bookbao.backend.common.properties.JwtProperties.*;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class LoginService {
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    /**
     * login 기능
     * 사용자로부터 포털 아이디, 비번, 이메일을 입력받아 id, pw는 "https://auth.imsejong.com/auth?method=DosejongSession" 여기로 보냄
     * 사용자의 이름, 학번 수준의 정보만 필요하므로 method=DosejongSession
     * 세종대 구성원 인증 통과 시 토큰 발급
     *
     * 세종대 구성원이 아니거나 이메일이 틀렸다면 에러 던짐
     */
    public void loginToPortal(UserLoginRequest userLoginRequest, HttpServletRequest httpServletRequest, HttpServletResponse response) throws BaseException {
        String url = "https://auth.imsejong.com/auth?method=DosejongSession";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HashMap<String ,String> parameters = new HashMap<>();
        parameters.put("id", userLoginRequest.getId());
        parameters.put("pw", userLoginRequest.getPw());

        HttpEntity<HashMap<String, String>> entity = new HttpEntity<>(parameters, headers);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        ResponseEntity<PortalApiResponse> portalApiResponse = restTemplate.postForEntity(url, entity, PortalApiResponse.class);
        if (portalApiResponse.getStatusCode() == HttpStatus.OK && portalApiResponse.getBody() != null) {
            boolean isAuth = portalApiResponse.getBody().getResult().isAuth();
            // 세종대학교 구성원 인증 통과 시 세션, user 생성
            if (isAuth) {
                HttpSession session = httpServletRequest.getSession();
                if (session.getAttribute("user") == null) {
                    session.setAttribute("user", userLoginRequest.getId());
                    log.info("[SESSION 생성]");

                    Optional<User> optionalUser = userRepository.findBySchoolId(session.getAttribute("user").toString());
                    User user = null;
                    if (optionalUser.isEmpty()) {
                        log.info("[처음 접속 user 생성]");
                        /** 처음 접속 시 user 생성 */
                        user = User.builder()
                                .name(portalApiResponse.getBody().getResult().getBody().getName())
                                .schoolId(userLoginRequest.getId())
                                .email(userLoginRequest.getEmail())
                                .build();
                        try {
                            userRepository.save(user);
                        } catch (BaseException e) {
                            throw new BaseException(BaseResponseStatus.DATABASE_INSERT_ERROR);
                        }
                    } else if (!optionalUser.get().getEmail().equals(userLoginRequest.getEmail())) {
                        throw new BaseException(BaseResponseStatus.NON_EXIST_EMAIL);
                    }
                    
                    // 토큰 처리 로직
                    if (optionalUser.isPresent()) user = optionalUser.get();
                    Map<String, String> tokens = jwtUtils.generateToken(user);


                    Cookie refreshCookie = new Cookie(JWT_REFRESH_TOKEN_COOKIE_NAME, tokens.get("refreshToken"));
                    refreshCookie.setMaxAge((int) REFRESH_TOKEN_EXPIRE_TIME); // 쿠키의 만료시간 설정
                    refreshCookie.setPath("/");
                    response.addCookie(refreshCookie);

                    // 토큰을 응답 헤더에 설정
                    response.addHeader(JWT_ACCESS_TOKEN_HEADER_NAME, JWT_ACCESS_TOKEN_TYPE + tokens.get("accessToken"));
                    response.setHeader(JWT_REFRESH_TOKEN_COOKIE_NAME, tokens.get("refreshToken"));

                } else {
                    Optional<User> optionalUser = userRepository.findBySchoolId(session.getAttribute("user").toString());
                    if (!optionalUser.get().getEmail().equals(userLoginRequest.getEmail())) {
                        throw new BaseException(BaseResponseStatus.NON_EXIST_EMAIL);
                    }
                }
            } else {        // 세종대 구성원이 아닌 경우
                throw new BaseException(BaseResponseStatus.NON_EXIST_USER);
            }
        }
        // 외부 api와 통신이 안된 경우
//         return null;
    }
}

