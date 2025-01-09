package io.zzinterior.zzinterior;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Token {
    @Id
    private String id;

    private String refreshToken;

    private String accessToken;

    public Token(String id, String refreshToken, String accessToken) {
        this.id = id;
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
    }

    public Token updateRefreshToken(String refreshToken){
        this.refreshToken=refreshToken;
        return this;
    }
    public void updateAccessToken(String accessToken){
        this.accessToken=accessToken;
    }
}
