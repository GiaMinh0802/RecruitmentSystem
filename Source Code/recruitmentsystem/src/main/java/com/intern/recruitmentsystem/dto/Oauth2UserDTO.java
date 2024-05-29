package com.fpt.recruitmentsystem.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
public class Oauth2UserDTO implements OAuth2User {
    private OAuth2User oAuth2User;
    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oAuth2User.getAuthorities();
    }

    @Override
    public String getName() {
        return oAuth2User.getAttribute("name");
    }

    public String getEmail(){
        return oAuth2User.getAttribute("email");
    }
    public String getImage(){
        return oAuth2User.getAttribute("picture");
    }

}
