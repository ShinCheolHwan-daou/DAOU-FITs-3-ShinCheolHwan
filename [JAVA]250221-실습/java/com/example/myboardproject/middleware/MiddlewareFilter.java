package com.example.myboardproject.middleware;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*")
public class MiddlewareFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;

        long startTime = System.currentTimeMillis(); // 요청 시작 시간 기록

        // 1. request, response 인코딩 타입 설정
        req.setCharacterEncoding("UTF-8");
        res.setCharacterEncoding("UTF-8");

        // 2. JSON response type 설정
        res.setContentType("application/json");

        // 3. JSON 요청인지 확인 (POST, PUT 요청만 체크)
        if ("POST".equalsIgnoreCase(req.getMethod()) || "PUT".equalsIgnoreCase(req.getMethod())) {
            if (!"application/json".equals(req.getContentType())) {
                res.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, "Content-Type must be application/json");
                return;
            }
        }

        // 4. 다음 필터 또는 서블릿 실행
        filterChain.doFilter(servletRequest, servletResponse);

        // 5. 실행 시간 로깅
        long duration = System.currentTimeMillis() - startTime;
        System.out.println("[Middleware] Request to " + req.getRequestURI() + " took " + duration + "ms");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
