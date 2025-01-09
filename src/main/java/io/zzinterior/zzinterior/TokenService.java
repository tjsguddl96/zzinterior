package io.zzinterior.zzinterior;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenRepository tokenRepository;

    public void deleteRefreshToken(String id){
        tokenRepository.deleteById(id);
    }
    @Transactional
    public void saveOrUpdate(String id,String refreshToken,String accessToken){
        Token token=tokenRepository.findByAccessToken(accessToken)
                .map(o->o.updateRefreshToken(refreshToken))
                .orElseGet(()->new Token(id,refreshToken,accessToken));
        tokenRepository.save(token);
    }
    public Token findByAccessTokenOrThrow(String accessToken){
        return tokenRepository.findByAccessToken(accessToken)
                .orElseThrow(() -> new NoSuchElementException("Token not found with access token: " + accessToken));
    }
    @Transactional
    public void updateToken(String accessToken,Token token){
        token.updateAccessToken(accessToken);
        tokenRepository.save(token);
    }
}
