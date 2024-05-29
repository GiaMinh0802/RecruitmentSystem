package com.fpt.recruitmentsystem.service.implement;

import com.fpt.recruitmentsystem.model.*;
import com.fpt.recruitmentsystem.service.IAuthenticationService;
import com.fpt.recruitmentsystem.service.IJwtService;
import com.fpt.recruitmentsystem.service.IMailService;
import com.fpt.recruitmentsystem.dto.LoginDTO;
import com.fpt.recruitmentsystem.dto.RegisterDTO;
import com.fpt.recruitmentsystem.dto.TokenDTO;
import com.fpt.recruitmentsystem.exception.*;
import com.fpt.recruitmentsystem.repository.*;
import com.fpt.recruitmentsystem.util.Message;
import com.fpt.recruitmentsystem.util.ResponseMessage;
import com.fpt.recruitmentsystem.util.Utility;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.internal.bytebuddy.utility.RandomString;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements IAuthenticationService {
    private final AccountRepository accountRepository;
    private final CandidateRepository candidateRepository;
    private final AdminRepository adminRepository;
    private final InterviewerRepository interviewerRepository;
    private final RecruiterRepository recruiterRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final IJwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;
    private final IMailService mailService;

    private static final String DEFAULT_AVT_LINK = "https://gamek.mediacdn.vn/133514250583805952/2021/6/24/photo-1-16245000005922003764148.jpg";
    private static final String DEFAULT_TYPE_AUTHENTICATION = "Bearer ";
    private static final String DOMAIN = "http://recruitment.ledangminh.id.vn";

    public ResponseMessage register(RegisterDTO registerDTO, String role) {
        boolean checkExist = accountRepository.existsAccountByEmail(registerDTO.getEmail());
        if (checkExist) {
            throw new ConflictException("Email already exists");
        }

        if (!Utility.checkEmail(registerDTO.getEmail())) {
            throw new BadRequestException("Email is invalid");
        }

        Account account = Account.builder()
                .email(registerDTO.getEmail())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .isActive(false)
                .role(roleRepository.findByName(role))
                .build();
        Account saveAccount = accountRepository.save(account);

        String lastname = RandomString.make(10);

        switch (role) {
            case "CANDIDATE":
                Candidate candidate = Candidate.builder()
                        .firstName("Candidate")
                        .lastName(lastname)
                        .linkAvt(DEFAULT_AVT_LINK)
                        .account(account)
                        .build();
                candidateRepository.save(candidate);
                break;
            case "RECRUITER":
                Recruiter recruiter = Recruiter.builder()
                        .firstName("Recruiter")
                        .lastName(lastname)
                        .linkAvt(DEFAULT_AVT_LINK)
                        .account(account)
                        .build();
                recruiterRepository.save(recruiter);
                break;
            case "INTERVIEWER":
                Interviewer interviewer = Interviewer.builder()
                        .firstName("Interviewer")
                        .lastName(lastname)
                        .linkAvt(DEFAULT_AVT_LINK)
                        .account(account)
                        .build();
                interviewerRepository.save(interviewer);
                break;
            default: break;
        }

        String activeToken = RandomString.make(30);

        Token token = Token.builder()
                .account(saveAccount)
                .accountToken(activeToken)
                .timeRequestedToken(new Date())
                .build();
        tokenRepository.save(token);

        String link = DOMAIN + "/verify-email/" + activeToken;
        mailService.sendActiveAccountLink(saveAccount.getEmail(), link);

        return new ResponseMessage(Message.SUCCESS);
    }

    public ResponseMessage registerAdmin(RegisterDTO registerDTO) {
        boolean checkExist = accountRepository.existsAccountByEmail(registerDTO.getEmail());
        if (checkExist) {
            throw new ConflictException("Email already exists");
        }
        Account account = Account.builder()
                .email(registerDTO.getEmail())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .role(roleRepository.findByName("ADMIN"))
                .isActive(true)
                .build();
        Account saveAccount = accountRepository.save(account);

        String lastname = RandomString.make(10);

        Admin admin = Admin.builder()
                .firstName("Admin")
                .lastName(lastname)
                .linkAvt(DEFAULT_AVT_LINK)
                .account(account)
                .build();
        adminRepository.save(admin);

        Token token = Token.builder()
                .account(saveAccount)
                .build();
        tokenRepository.save(token);

        return new ResponseMessage(Message.SUCCESS);
    }

    public ResponseMessage resendActiveAccount(String email) {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(Message.ACCOUNT_NOT_FOUND));
        if (account.isActive())
            throw new BadRequestException("Account has been activated");
        Token token = tokenRepository.findTokenByAccountId(account.getId());
        String activeToken = RandomString.make(30);
        token.setAccountToken(activeToken);
        token.setTimeRequestedToken(new Date());
        tokenRepository.save(token);
        String link = DOMAIN + "/verify-email/" + activeToken;
        mailService.sendActiveAccountLink(email, link);
        return new ResponseMessage(Message.SUCCESS);
    }

    public ResponseMessage activeAccount(String token) {
        Token activeToken = tokenRepository.findByAccountToken(token)
                .orElseThrow(() -> new BadRequestException("Invalid token"));
        Account account = activeToken.getAccount();
        if (account.isActive())
            throw new BadRequestException("Account has been activated");
        long currentTimeInMillis = System.currentTimeMillis();
        long requestedTimeInMillis = activeToken.getTimeRequestedToken().getTime();
        if (requestedTimeInMillis + (1000 * 60 * 60 * 24) < currentTimeInMillis) {
            activeToken.setAccountToken(null);
            activeToken.setTimeRequestedToken(null);
            tokenRepository.save(activeToken);
            throw new GoneException("Expired link");
        }
        activeAccount(account, activeToken);
        return new ResponseMessage(Message.SUCCESS);
    }

    public TokenDTO login(LoginDTO loginDTO) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));
        Account account = accountRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new NotFoundException(Message.ACCOUNT_NOT_FOUND));
        if (!account.isActive())
            throw new BadRequestException("Account is not activated");
        String accessToken = jwtService.generateToken(account);
        String refreshToken = jwtService.generateRefreshToken(account);
        updateAccountToken(account, refreshToken);
        return TokenDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public ResponseMessage logout(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith(DEFAULT_TYPE_AUTHENTICATION)) {
            throw new BadRequestException("No token to logout");
        }
        String jwt = authHeader.substring(7);
        String email = jwtService.extractUsername(jwt);
        Token storedToken = tokenRepository.findTokenByAccountEmail(email).orElse(null);
        if (storedToken != null) {
            storedToken.setToken(null);
            tokenRepository.save(storedToken);
            SecurityContextHolder.clearContext();
        } else
            throw new NotFoundException("Token not found");
        return new ResponseMessage("Logged out");
    }

    public Object refreshToken(HttpServletRequest request){
        final String accountEmail;
        final Date timeExiration;
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith(DEFAULT_TYPE_AUTHENTICATION)) {
            throw new BadRequestException("No token to refresh");
        }
        String refreshToken = authHeader.substring(7);
        try {
            accountEmail = jwtService.extractUsername(refreshToken);
            timeExiration = jwtService.extractExiration(refreshToken);
        } catch (ExpiredJwtException e) {
            return "Token has expired";
        }
        Account account = accountRepository.findByEmail(accountEmail)
                .orElseThrow(() -> new NotFoundException(Message.ACCOUNT_NOT_FOUND));
        Token token = tokenRepository.findTokenByAccountId(account.getId());
        if (!refreshToken.equals(account.getToken().getToken()))
            throw new BadRequestException("Invalid token refresh");
        if (jwtService.isTokenValid(refreshToken, account)) {
            String accessToken = jwtService.generateToken(account);
            String newRefreshToken = jwtService.generateNewRefreshToken(timeExiration, account);
            token.setToken(newRefreshToken);
            tokenRepository.save(token);
            return TokenDTO.builder()
                    .accessToken(accessToken)
                    .refreshToken(newRefreshToken)
                    .build();
        } else {
            throw new BadRequestException("Invalid token refresh");
        }
    }

    public ResponseMessage forgotPassword(String email) {
        if (!Utility.checkEmail(email)) {
            throw new BadRequestException("Email is invaild");
        }
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(Message.ACCOUNT_NOT_FOUND));
        String resetPasswordToken = RandomString.make(30);
        String link = DOMAIN + "/reset-password/" + resetPasswordToken;
        mailService.sendResetPasswordLink(email, link);
        Token token = tokenRepository.findTokenByAccountId(account.getId());
        token.setAccountToken(resetPasswordToken);
        token.setTimeRequestedToken(new Date());
        tokenRepository.save(token);
        return new ResponseMessage(Message.SUCCESS);
    }

    public ResponseMessage resetPassword(String token, String password) {
        Token findToken = tokenRepository.findByAccountToken(token)
                .orElseThrow(() -> new BadRequestException("Invalid token"));
        Account account = accountRepository.findById(findToken.getAccount().getId())
            .orElseThrow(() -> new NotFoundException(Message.ACCOUNT_NOT_FOUND));
        long currentTimeInMillis = System.currentTimeMillis();
        long requestedTimeInMillis = findToken.getTimeRequestedToken().getTime();
        if (requestedTimeInMillis + (1000 * 60 * 5) < currentTimeInMillis) {
            findToken.setAccountToken(null);
            findToken.setTimeRequestedToken(null);
            tokenRepository.save(findToken);
            throw new GoneException("Expired link");
        }
        updatePassword(account, findToken, password);
        return new ResponseMessage(Message.SUCCESS);
    }

    public ResponseMessage changePassword(HttpServletRequest request, String password) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith(DEFAULT_TYPE_AUTHENTICATION)) {
            throw new ForbiddenException("Access Denied");
        }
        String jwt = authHeader.substring(7);
        String email = jwtService.extractUsername(jwt);
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(Message.ACCOUNT_NOT_FOUND));
        account.setPassword(passwordEncoder.encode(password));
        accountRepository.save(account);
        return new ResponseMessage(Message.SUCCESS);
    }

    private void updateAccountToken(Account account, String refreshToken) {
        Token validAccountToken = tokenRepository.findTokenByAccountId(account.getId());
        if (validAccountToken == null)
            throw new NotFoundException(Message.ACCOUNT_NOT_FOUND);
        validAccountToken.setToken(refreshToken);
        tokenRepository.save(validAccountToken);
    }

    private void updatePassword(Account account, Token token, String newPassword) {
        account.setPassword(passwordEncoder.encode(newPassword));
        accountRepository.save(account);

        token.setAccountToken(null);
        token.setTimeRequestedToken(null);
        tokenRepository.save(token);
    }

    private void activeAccount(Account account, Token token) {
        account.setActive(true);
        accountRepository.save(account);

        token.setAccountToken(null);
        token.setTimeRequestedToken(null);
        tokenRepository.save(token);
    }

}

