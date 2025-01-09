package io.zzinterior.zzinterior;

import io.zzinterior.zzinterior.entity.Role;
import io.zzinterior.zzinterior.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuth2UserInfo {
    private String name;
    private String email;
    private static final String ILLEGAL_REGISTRATION_ID = "Illegal registration ID";  // 상수 정의

    @Builder
    public OAuth2UserInfo(String name,String email){
        this.name=name;
        this.email=email;
    }
    public static OAuth2UserInfo of(String registrationId, Map<String,Object> attributes){
        return switch(registrationId){
            case "google"->ofGoogle(attributes);
            default -> throw new IllegalArgumentException(ILLEGAL_REGISTRATION_ID);
        };
    }
    private static OAuth2UserInfo ofGoogle(Map<String,Object> attributes){
        return OAuth2UserInfo.builder()
                .name((String)attributes.get("name"))
                .email((String)attributes.get("email"))
                .build();
    }
    public User toEntity(){
        return User.builder()
                .name(name)
                .email(email)
                .role(Role.USER)
                .build();
    }

}
