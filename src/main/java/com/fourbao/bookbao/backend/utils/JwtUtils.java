package com.fourbao.bookbao.backend.utils;

import com.fourbao.bookbao.backend.common.exception.BaseException;
import com.fourbao.bookbao.backend.common.response.BaseResponseStatus;
import com.fourbao.bookbao.backend.entity.User;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.Jwts;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.fourbao.bookbao.backend.common.properties.JwtProperties.ACCESS_TOKEN_EXPIRE_TIME;
import static com.fourbao.bookbao.backend.common.properties.JwtProperties.REFRESH_TOKEN_EXPIRE_TIME;

// JWT 토큰 유틸리티 클래스

@Slf4j
@Component
public class JwtUtils {

    private final Key key;

    public JwtUtils(@Value("${jwt.secret}") String jwtSecret) {
        try {
            byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
            this.key = Keys.hmacShaKeyFor(keyBytes);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Invalid JWT secret key", e);
        }
    }

    // 토큰을 생성해주는 기능
    public Map<String, String> generateToken (User user) {
        String accessToken = Jwts.builder()
                .setHeaderParam("alg", "HS256")
                .setHeaderParam("typ", "JWT")
                .claim("uid", user.getId())
                .claim("userSchoolId", user.getSchoolId())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        String refreshToken = Jwts.builder()
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        Map<String, String> tokenInfo = new HashMap<>();
        tokenInfo.put("accessToken", accessToken);
        tokenInfo.put("refreshToken", refreshToken);

        return tokenInfo;
    }

    // 토큰에서 User ID 추출
    public String getUserSchoolId(String accessToken) throws BaseException {
        try {
            String schoolId = Jwts
                    .parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody()
                    .get("userSchoolId", String.class);
            return schoolId;
        } catch (ExpiredJwtException jwtException) {
            throw new BaseException(BaseResponseStatus.EXPIRED_JWT_TOKEN);
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.INVALID_JWT_TOKEN);
        }
    }

    public Long getUserId(String accessToken) throws BaseException {
        try {
            Long id = Jwts
                    .parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody()
                    .get("uid", Long.class);
            return id;
        } catch (ExpiredJwtException expiredJwt) {
            throw new BaseException(BaseResponseStatus.EXPIRED_JWT_TOKEN);
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.INVALID_JWT_TOKEN);
        }
    }
}
