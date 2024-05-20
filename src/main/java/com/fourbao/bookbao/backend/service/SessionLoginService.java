package com.fourbao.bookbao.backend.service;

import com.fourbao.bookbao.backend.common.exception.BaseException;
import com.fourbao.bookbao.backend.common.response.BaseResponseStatus;
import com.fourbao.bookbao.backend.dto.request.UserLoginRequest;
import com.fourbao.bookbao.backend.dto.response.PortalApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Optional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class SessionLoginService {

    public void loginToPortal(UserLoginRequest userLoginRequest, HttpServletRequest httpServletRequest) throws BaseException {
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
                }
            } else {
                throw new BaseException(BaseResponseStatus.NON_EXIST_USER);
            }
        }
    }
}

