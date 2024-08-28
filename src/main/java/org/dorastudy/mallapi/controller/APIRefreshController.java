package org.dorastudy.mallapi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.dorastudy.mallapi.util.CustomJWTException;
import org.dorastudy.mallapi.util.JWTUtil;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
public class APIRefreshController {
    @RequestMapping("/api/member/refresh")
    public Map<String, Object> refresh(
            @RequestHeader("Authorization") String authHeader,
            String refreshToken
    ) {
        if (refreshToken == null) {
            throw new CustomJWTException("NO_REFRESH_TOKEN");
        }
        if (authHeader == null || authHeader.length() < 7) {
            throw new CustomJWTException("INVALID_STRING");
        }

        // Bearer ~
        String accessToken = authHeader.substring(7);

        // Access Token 만료 여부 확인
        if(!checkExpiredToken(accessToken)) {
            return Map.of("accessToken", accessToken, "refreshToken", refreshToken);
        }
        // Refresh Token 검증
        Map<String, Object> claims = JWTUtil.validateToken(refreshToken);
        log.info("Refresh token Claims: {}", claims);

        String newAccessToken = JWTUtil.generateToken(claims, 10);
        String newRefreshToken = checkTime((Integer) claims.get("exp")) ? JWTUtil.generateToken(claims, 60 * 24) : refreshToken;

        return Map.of("accessToken", newAccessToken, "refreshToken", newRefreshToken);
    }

    // 만료 시간이 1시간 미만인지 확인
    private boolean checkTime(Integer exp) {
        // JWT exp -> 날짜 변환
        Date expDate = new Date((long) exp * 1000);

        // 현재 시간과 차이 계산
        long gap = expDate.getTime() - System.currentTimeMillis();

        // 분 단위로 변환
        long leftMin = gap / (1000 * 60);

        // 1시간 미만인지 확인
        return leftMin < 60;
    }

    // Token 만료 여부 확인
    private boolean checkExpiredToken(String token) {
        try {
            JWTUtil.validateToken(token);
        } catch (CustomJWTException e) {
            if (e.getMessage().equals("Expired")) {
                return true; // 만료
            }
        }
        return false; // 만료 되지 않음
    }
}
