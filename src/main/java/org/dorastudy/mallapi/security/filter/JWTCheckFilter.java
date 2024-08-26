package org.dorastudy.mallapi.security.filter;

import com.google.gson.Gson;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.dorastudy.mallapi.util.JWTUtil;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Log4j2
public class JWTCheckFilter extends OncePerRequestFilter {

    // 검사를 하지 않아도 되는 경로로 들어올 때 사용
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // 검사를 안 하는 건지

        // ture ==> 검사하지 않음
        String path = request.getRequestURI();
        log.info("check uri: {}", path);

        // false ==> 검사함
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        log.info("---------- do filter internal start ----------");

        try {
            String authHeaderStr = request.getHeader("Authorization");

            // Bearer // 7 JWT문자열
            String accessToken = authHeaderStr.substring(7);
            Map<String, Object> claims = JWTUtil.validateToken(accessToken);

            log.info("claims: {}", claims);

            // destination
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("JWT Check Error: {}", e.getMessage());

            Gson gson = new Gson();
            String msg = gson.toJson(Map.of("error", "ERROR_ACCESS_TOKEN"));

            response.setContentType("application/json");
            PrintWriter printWriter = response.getWriter();
            printWriter.println(msg);
            printWriter.close();
        }
        log.info("---------- do filter internal end ----------");
    }
}
