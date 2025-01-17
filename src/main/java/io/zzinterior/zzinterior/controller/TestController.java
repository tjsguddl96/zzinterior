package io.zzinterior.zzinterior.controller;


import io.zzinterior.zzinterior.PrincipalDetails;
import io.zzinterior.zzinterior.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@Controller
@RequiredArgsConstructor
public class TestController {

    @GetMapping("/")
    public String home(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            System.out.println("Authenticated user: " + authentication.getName());
        } else {
            System.out.println("No authenticated user found");
        }
        if (principalDetails != null) {
            model.addAttribute("userName", principalDetails.getUser().getName());
        } else {
            model.addAttribute("userName", null);
        }
        return "home";
    }

    // 로그인 성공 후 리다이렉션
    @GetMapping("/auth/success")
    public String loginSuccess() {
        System.out.println("리다이렉트");
        return "redirect:/";
    }
}