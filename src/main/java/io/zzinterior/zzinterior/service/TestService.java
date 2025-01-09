package io.zzinterior.zzinterior.service;

import io.zzinterior.zzinterior.OAuth2UserInfo;
import io.zzinterior.zzinterior.PrincipalDetails;
import io.zzinterior.zzinterior.entity.User;
import io.zzinterior.zzinterior.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class TestService extends DefaultOAuth2UserService {
    private final TestRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        //유저 정보(attributes) 가져오기
        Map<String,Object> oAuth2UserAttributes=super.loadUser(userRequest).getAttributes();

        //registraionId 가져오기 (third-party id)
        String registrationId=userRequest.getClientRegistration().getRegistrationId();

        //userNameAttributeName 가져오기
        String userNameAttributeName=userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        //유저 정보 dto생성
        OAuth2UserInfo oAuth2UserInfo=OAuth2UserInfo.of(registrationId,oAuth2UserAttributes);

        //회원가입 및 로그인
        User user=getOrSave(oAuth2UserInfo);

        //OAuth2User로 반환
        return new PrincipalDetails(user,oAuth2UserAttributes,userNameAttributeName);

    }
    private User getOrSave(OAuth2UserInfo oAuth2UserInfo){
        User user=userRepository.findByEmail(oAuth2UserInfo.getEmail())
                .orElseGet(oAuth2UserInfo::toEntity);
        return userRepository.save(user);
    }

}
