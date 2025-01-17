package io.zzinterior.zzinterior;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Component
public class TokenProvider {

    @Value("${jwt.key}")
    private String key;
    private SecretKey secretKey;
    private static final long ACCESS_TOKEN_EXPIRE_TIME=1000*60*30L;
    private static final long REFRESH_TOKEN_EXPIRE_TIME=1000*60*60L*24*7;
    private static final String KEY_ROLE="role";
    private final TokenService tokenService;
    @PostConstruct
    private void setSecretKey(){
        secretKey= Keys.hmacShaKeyFor(key.getBytes());
    }

    public String generateAccessToken(Authentication authentication){
        String token=generateToken(authentication,ACCESS_TOKEN_EXPIRE_TIME);
        System.out.println("token은 "+token);
//        return generateToken(authentication,ACCESS_TOKEN_EXPIRE_TIME);
        return token;
    }

    //refresh token 발급
    public  void generateRefreshToken(Authentication authentication,String accessToken){
        String refreshToken=generateToken(authentication,REFRESH_TOKEN_EXPIRE_TIME);
        tokenService.saveOrUpdate(authentication.getName(),refreshToken,accessToken);
    }
    private String generateToken(Authentication authentication,long expireTime){
        Date now=new Date();
        Date expireDate=new Date(now.getTime()+expireTime);
        String authorities=authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining());
        System.out.println("generate Token!!!!");
        return Jwts.builder()
                .subject(authentication.getName())
                .claim(KEY_ROLE,authorities)
                .issuedAt(now)
                .expiration(expireDate)
                .signWith(secretKey,Jwts.SIG.HS256)
                .compact();
    }

    public Authentication getAuthentication(String token){
        Claims claims=parseClaims(token);
        List<SimpleGrantedAuthority> authorities=getAuthorites(claims);

        //security의 user 객체 생성
        User principal=new User(claims.getSubject(),"",authorities);
        return new UsernamePasswordAuthenticationToken(principal,token,authorities);
    }

    private List<SimpleGrantedAuthority> getAuthorites(Claims claims){
        return Collections.singletonList(new SimpleGrantedAuthority(claims.get(KEY_ROLE).toString()));
    }

    //accessToken 재발급
    public String reissueAccessToken(String accessToken){
        if(StringUtils.hasText(accessToken)){
            Token token=tokenService.findByAccessTokenOrThrow(accessToken);
            String refreshToken=token.getRefreshToken();

            if(validateToken(refreshToken)){
                String reissueAccessToken=generateAccessToken(getAuthentication(refreshToken));
                tokenService.updateToken(reissueAccessToken,token);
                return reissueAccessToken;
            }
        }
        return null;
    }

    public boolean validateToken(String token){
        if(!StringUtils.hasText(token)){
            return false;
        }
        Claims claims=parseClaims(token);
        return claims.getExpiration().after(new Date());
    }

    private Claims parseClaims(String token){
        try{
            return Jwts.parser().verifyWith(secretKey).build()
                    .parseSignedClaims(token).getPayload();
        }catch (ExpiredJwtException e){
            return e.getClaims();
        }catch (MalformedJwtException e){
            throw new IllegalArgumentException("Malformed JWT token", e);
        } catch (SecurityException e) {
            throw new JwtException("Invalid JWT signature", e);
        }
    }

}
