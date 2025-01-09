package io.zzinterior.zzinterior;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class TokenExceptionFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            filterChain.doFilter(request,response);
        }catch (RuntimeException e) {  // TokenException을 RuntimeException으로 대체
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid token: " + e.getMessage());
        }
    }
}
