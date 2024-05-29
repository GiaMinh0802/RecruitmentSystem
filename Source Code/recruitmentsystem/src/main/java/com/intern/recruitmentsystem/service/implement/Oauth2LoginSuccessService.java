package com.fpt.recruitmentsystem.service.implement;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fpt.recruitmentsystem.model.Account;
import com.fpt.recruitmentsystem.model.Candidate;
import com.fpt.recruitmentsystem.model.Token;
import com.fpt.recruitmentsystem.dto.Oauth2UserDTO;
import com.fpt.recruitmentsystem.dto.TokenDTO;
import com.fpt.recruitmentsystem.exception.NotFoundException;
import com.fpt.recruitmentsystem.repository.*;
import com.fpt.recruitmentsystem.service.IJwtService;

import com.fpt.recruitmentsystem.service.IMailService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class Oauth2LoginSuccessService extends SimpleUrlAuthenticationSuccessHandler {
    private final AccountRepository accountRepository;
    private final CandidateRepository candidateRepository;
    private final RoleRepository roleRepository;
    private final IJwtService jwtService;
    private final TokenRepository tokenRepository;
    private final IMailService mailService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Oauth2UserDTO oauth2User = (Oauth2UserDTO) authentication.getPrincipal();
        String email = oauth2User.getEmail();
        String name = oauth2User.getName();
        String imageUrl = oauth2User.getImage();

        int spaceIndex = name.indexOf(' ');
        String firstName = name.substring(0, spaceIndex);
        String lastName = name.substring(spaceIndex+1);

        boolean checkExist = accountRepository.existsAccountByEmail(email);
        if (!checkExist) {
            new ObjectMapper().writeValue(response.getOutputStream(), tokenBuilderNonExistAccount(email, firstName, lastName, imageUrl));
            return;
        }
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Account not found"));
        new ObjectMapper().writeValue(response.getOutputStream(), tokenBuilderExistAccount(account));

        super.onAuthenticationSuccess(request, response, authentication);
    }

    private final TokenDTO tokenBuilderExistAccount(Account account){
        String accessToken = jwtService.generateToken(account);
        String refreshToken = jwtService.generateRefreshToken(account);

        Token validAccountToken = tokenRepository.findTokenByAccountId(account.getId());
        if (validAccountToken == null)
            throw new NotFoundException("Token not found!");
        validAccountToken.setToken(refreshToken);
        tokenRepository.save(validAccountToken);

        return TokenDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
    private final TokenDTO tokenBuilderNonExistAccount(String email, String firstName, String lastName, String imageUrl){
        Account account = Account.builder()
                .email(email)
                .role(roleRepository.findByName("CANDIDATE"))
                .isActive(true)
                .build();
        Account saveAccount = accountRepository.save(account);
        Candidate candidate = Candidate.builder()
                .account(account)
                .firstName(firstName)
                .lastName(lastName)
                .linkAvt(imageUrl)
                .build();
        candidateRepository.save(candidate);

        String accessToken = jwtService.generateToken(account);
        String refreshToken = jwtService.generateRefreshToken(account);

        Token token = Token.builder()
                .account(saveAccount)
                .token(refreshToken)
                .build();
        tokenRepository.save(token);

        mailService.sendLoginGoogleSuccess(email, firstName, lastName);

        return TokenDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}

