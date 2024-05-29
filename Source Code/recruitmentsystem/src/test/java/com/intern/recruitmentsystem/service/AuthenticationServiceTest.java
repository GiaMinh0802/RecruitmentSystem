package com.fpt.recruitmentsystem.service;

import com.fpt.recruitmentsystem.model.Account;
import com.fpt.recruitmentsystem.model.Candidate;
import com.fpt.recruitmentsystem.dto.LoginDTO;
import com.fpt.recruitmentsystem.dto.RegisterDTO;
import com.fpt.recruitmentsystem.dto.TokenDTO;
import com.fpt.recruitmentsystem.exception.BadRequestException;
import com.fpt.recruitmentsystem.exception.ConflictException;
import com.fpt.recruitmentsystem.exception.GoneException;
import com.fpt.recruitmentsystem.exception.NotFoundException;
import com.fpt.recruitmentsystem.repository.*;
import com.fpt.recruitmentsystem.service.implement.AuthenticationService;
import com.fpt.recruitmentsystem.util.ResponseMessage;
import com.fpt.recruitmentsystem.util.Utility;

import jakarta.servlet.http.HttpServletRequest;

import com.fpt.recruitmentsystem.model.Role;

import com.fpt.recruitmentsystem.model.Token;

import com.fpt.recruitmentsystem.model.Admin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {

	@Mock
	AccountRepository accountRepository;

	@Mock
	CandidateRepository candidateRepository;

	@Mock
	AdminRepository adminRepository;

	@Mock
	InterviewerRepository interviewerRepository;

	@Mock
	RecruiterRepository recruiterRepository;

	@Mock
	RoleRepository roleRepository;

	@Mock
	IJwtService jwtService;

	@Mock
	AuthenticationManager authenticationManager;

	@Mock
	PasswordEncoder passwordEncoder;

	@Mock
	TokenRepository tokenRepository;

	@Mock
	IMailService mailService;

	@Mock
	private Utility utility;

	@InjectMocks
	AuthenticationService authenticationService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testRegister_WhenEmailAlreadyExists_ShouldThrowConflictException() {
		RegisterDTO registerDTO = new RegisterDTO();
		registerDTO.setEmail("test@test.com");
		when(accountRepository.existsAccountByEmail(any())).thenReturn(true);

		assertThrows(ConflictException.class, () -> authenticationService.register(registerDTO, "CANDIDATE"));

		verify(accountRepository, times(1)).existsAccountByEmail(any());
		verifyNoMoreInteractions(accountRepository, candidateRepository, recruiterRepository, interviewerRepository,
				roleRepository, jwtService, authenticationManager, passwordEncoder, tokenRepository, mailService);
	}

	@Test
	void testRegister_WhenEmailIsInvalid_ShouldThrowBadRequestException() {
		RegisterDTO registerDTO = new RegisterDTO();
		registerDTO.setEmail("invalidemail");
		when(accountRepository.existsAccountByEmail(any())).thenReturn(false);

		assertThrows(BadRequestException.class, () -> authenticationService.register(registerDTO, "CANDIDATE"));

		verify(accountRepository, times(1)).existsAccountByEmail(any());
		verifyNoMoreInteractions(accountRepository, candidateRepository, recruiterRepository, interviewerRepository,
				roleRepository, jwtService, authenticationManager, passwordEncoder, tokenRepository, mailService);
	}

	@Test
	void testRegister_WhenEmailIsValid_ShouldCreateAccount() {
		RegisterDTO registerDTO = new RegisterDTO();
		registerDTO.setEmail("camnguyentqnk@gmail.com");
		registerDTO.setPassword("password");

		Role candidateRole = new Role();
		candidateRole.setName("CANDIDATE");

		Account savedAccount = Account.builder().email(registerDTO.getEmail()).password(registerDTO.getPassword())
				.role(candidateRole).isActive(true).build();

		Candidate savedCandidate = Candidate.builder().account(savedAccount).build();

		Token savedToken = Token.builder().account(savedAccount).build();

		when(accountRepository.existsAccountByEmail(any())).thenReturn(false);
		when(roleRepository.findByName(anyString())).thenReturn(candidateRole);
		when(accountRepository.save(any(Account.class))).thenReturn(savedAccount);
		when(candidateRepository.save(any(Candidate.class))).thenReturn(savedCandidate);
		when(tokenRepository.save(any(Token.class))).thenReturn(savedToken);
		when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
//        when(Utility.checkEmail(anyString())).thenReturn(true);

		ResponseMessage responseMessage = authenticationService.register(registerDTO, "CANDIDATE");

		assertNotNull(responseMessage);
		assertEquals("Success", responseMessage.getMessage());

		verify(accountRepository, times(1)).existsAccountByEmail(any());
		verify(roleRepository, times(1)).findByName(anyString());
		verify(accountRepository, times(1)).save(any(Account.class));
		verify(candidateRepository, times(1)).save(any(Candidate.class));
		verify(tokenRepository, times(1)).save(any(Token.class));
		verify(passwordEncoder, times(1)).encode(anyString());

		verify(mailService, times(1)).sendActiveAccountLink(anyString(), anyString());
		verifyNoMoreInteractions(accountRepository, roleRepository, candidateRepository, recruiterRepository,
				interviewerRepository, tokenRepository, passwordEncoder, mailService, utility);
	}

	@Test
	void testRegisterAdmin_WhenEmailDoesNotExist_ShouldCreateAdmin() {
		RegisterDTO registerDTO = new RegisterDTO();
		registerDTO.setEmail("test@test.com");
		registerDTO.setPassword("password");

		Role adminRole = new Role();
		adminRole.setName("ADMIN");

		Account savedAccount = Account.builder()

				.email(registerDTO.getEmail()).password(passwordEncoder.encode(registerDTO.getPassword()))
				.role(adminRole).isActive(true).build();

		Admin savedAdmin = Admin.builder().account(savedAccount).build();

		Token savedToken = Token.builder().account(savedAccount).build();

		when(accountRepository.existsAccountByEmail(any())).thenReturn(false);
		when(roleRepository.findByName("ADMIN")).thenReturn(adminRole);
		when(accountRepository.save(any(Account.class))).thenReturn(savedAccount);
		when(adminRepository.save(any(Admin.class))).thenReturn(savedAdmin);
		when(tokenRepository.save(any(Token.class))).thenReturn(savedToken);

		ResponseMessage responseMessage = authenticationService.registerAdmin(registerDTO);

		assertNotNull(responseMessage);
		assertEquals("Success", responseMessage.getMessage());

		verify(accountRepository, times(1)).existsAccountByEmail(any());
		verify(roleRepository, times(1)).findByName("ADMIN");
		verify(accountRepository, times(1)).save(any(Account.class));
		verify(adminRepository, times(1)).save(any(Admin.class));
		verify(tokenRepository, times(1)).save(any(Token.class));
		verifyNoMoreInteractions(accountRepository, roleRepository, adminRepository, tokenRepository, mailService);
	}

	@Test
	void testLogin_WhenAccountNotFound_ShouldThrowNotFoundException() {
		LoginDTO loginDTO = new LoginDTO("example@example.com", "password");

		when(accountRepository.findByEmail(loginDTO.getEmail())).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> authenticationService.login(loginDTO));

		verify(accountRepository, times(1)).findByEmail(loginDTO.getEmail());
		verifyNoMoreInteractions(accountRepository);
	}

	@Test
	void testLogin_WhenAccountIsNotActivated_ShouldThrowBadRequestException() {
		LoginDTO loginDTO = new LoginDTO("example@example.com", "password");

		Account account = new Account();
		account.setActive(false);

		when(accountRepository.findByEmail(loginDTO.getEmail())).thenReturn(Optional.of(account));

		assertThrows(BadRequestException.class, () -> authenticationService.login(loginDTO));

		verify(accountRepository, times(1)).findByEmail(loginDTO.getEmail());
		verifyNoMoreInteractions(accountRepository);
	}

	@Test
	void testLogin_WhenAccountIsValid_ShouldReturnTokenDTO() {
		// Create a new account and save it to the database
		Account account = new Account();
		account.setId(1);
		account.setEmail("test@example.com");
		account.setPassword(passwordEncoder.encode("password"));
		account.setActive(true);

		when(accountRepository.findByEmail("test@example.com")).thenReturn(Optional.of(account));
		when(tokenRepository.findTokenByAccountId(1)).thenReturn(Token.builder().build());
		when(jwtService.generateToken(account)).thenReturn("Access Token");

		// Test case 1: When account is valid, should return a TokenDTO
		LoginDTO validLoginDTO = new LoginDTO();
		validLoginDTO.setEmail("test@example.com");
		validLoginDTO.setPassword("password");
		TokenDTO tokenDTO = authenticationService.login(validLoginDTO);
		assertNotNull(tokenDTO.getAccessToken());
	}

	@Test
	void testLogin_WhenEmailIsInvalid_ShouldThrowNotFoundException() {

		LoginDTO invalidEmailDTO = new LoginDTO();
		invalidEmailDTO.setEmail("invalid@example.com");
		invalidEmailDTO.setPassword("password");
		assertThrows(NotFoundException.class, () -> {
			authenticationService.login(invalidEmailDTO);
		});
	}

	@Test
	void testLogin_WhenPasswordIsInvalid_ShouldThrowBadCredentialsException() {

		LoginDTO invalidPasswordDTO = new LoginDTO();
		invalidPasswordDTO.setEmail("test@example.com");
		invalidPasswordDTO.setPassword("invalid");
		assertThrows(NotFoundException.class, () -> {
			authenticationService.login(invalidPasswordDTO);
		});
	}

	@Test
	public void testSetEmail() {

		Account account = new Account();
		account.setEmail("test.email@example.com");
		account.setPassword("testpassword");
		account.setActive(true);
		assertEquals("test.email@example.com", account.getEmail());
		account.setEmail("new.email@example.com");
		assertEquals("new.email@example.com", account.getEmail());
	}

	@Test
	void testActiveAccount_WhenTokenIsInvalid_ShouldThrowBadRequestException() {
		String invalidToken = "invalid_token";

		when(tokenRepository.findByAccountToken(invalidToken)).thenReturn(Optional.empty());
		assertThrows(BadRequestException.class, () -> authenticationService.activeAccount(invalidToken));
		verify(tokenRepository, times(1)).findByAccountToken(invalidToken);
		verifyNoMoreInteractions(tokenRepository);
	}

	@Test
	void testActiveAccount_WhenAccountIsActive_ShouldThrowBadRequestException() {
		String validToken = "valid_token";

		Account account = new Account();
		account.setActive(true);

		Token activationToken = Token.builder().accountToken(validToken).account(account)
				.timeRequestedToken(new Timestamp(System.currentTimeMillis())).build();

		when(tokenRepository.findByAccountToken(validToken)).thenReturn(Optional.of(activationToken));
		assertThrows(BadRequestException.class, () -> authenticationService.activeAccount(validToken));

		verify(tokenRepository, times(1)).findByAccountToken(validToken);
		verifyNoMoreInteractions(tokenRepository);
	}

	@Test
	void testActiveAccount_WhenLinkIsExpired_ShouldThrowGoneException() {
		String validToken = "valid_token";

		Account account = new Account();
		account.setActive(false);

		Token activationToken = Token.builder().accountToken(validToken).account(account)
				.timeRequestedToken(new Timestamp(System.currentTimeMillis() - (1000 * 60 * 60 * 24 * 2))).build();

		when(tokenRepository.findByAccountToken(validToken)).thenReturn(Optional.of(activationToken));

		assertThrows(GoneException.class, () -> authenticationService.activeAccount(validToken));

		verify(tokenRepository, times(1)).findByAccountToken(validToken);
		verify(tokenRepository, times(1)).save(activationToken);
		verifyNoMoreInteractions(tokenRepository);
	}

	@Test
	void testActivateAccount_WhenActivationIsSuccessful_ShouldActivateAccount() {
		// Arrange
		Account account = new Account();
		account.setId(1);
		Token mockToken = new Token();
		mockToken.setAccount(account);
		mockToken.setTimeRequestedToken(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
		mockToken.setToken("mocktoken");
		when(tokenRepository.findByToken("mocktoken")).thenReturn(Optional.of(mockToken));
		when(tokenRepository.findByAccountToken(any())).thenReturn(Optional.of(mockToken));
		ResponseMessage responseMessage = authenticationService.activeAccount("mocktoken");

		assertEquals("Success", responseMessage.getMessage());
		assertTrue(account.isActive());

		verify(tokenRepository, times(1)).findByAccountToken("mocktoken");

		verify(accountRepository, times(1)).save(account);

	}

	@Test
	public void testLogoutWithValidToken() {
		// Arrange
		HttpServletRequest request = mock(HttpServletRequest.class);
		String token = "Bearer your-valid-token";
		String email = "example@example.com";
		Token storedToken = new Token();

		when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(token);
		when(jwtService.extractUsername("your-valid-token")).thenReturn(email);
		when(tokenRepository.findTokenByAccountEmail(email)).thenReturn(Optional.of(storedToken));

		// Act
		ResponseMessage responseMessage = authenticationService.logout(request);

		// Assert
		assertEquals("Logged out", responseMessage.getMessage());
		assertEquals(null, storedToken.getToken()); // Ensure the token was cleared
		verify(tokenRepository, times(1)).save(storedToken);
	}

	@Test
	public void testLogoutWithInvalidToken() {
		// Arrange
		String token = "InvalidToken";
		HttpServletRequest request = mock(HttpServletRequest.class);

		when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(token);

		// Act and Assert
		assertThrows(BadRequestException.class, () -> authenticationService.logout(request));
	}

	@Test
	public void testLogoutWithTokenNotFound() {
		// Arrange
		HttpServletRequest request = mock(HttpServletRequest.class);
		String token = "Bearer your-valid-token";
		String email = "example@example.com";

		when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(token);
		when(jwtService.extractUsername("your-valid-token")).thenReturn(email);
		when(tokenRepository.findTokenByAccountEmail(email)).thenReturn(Optional.empty());

		// Act and Assert
		assertThrows(NotFoundException.class, () -> authenticationService.logout(request));
	}

	@Test
	public void testResendActiveAccountWithInvalidEmail() {
		// Arrange
		String email = "invalid-email";

		// Act and Assert
		assertThrows(BadRequestException.class, () -> authenticationService.resendActiveAccount(email));
		verify(accountRepository, never()).findByEmail(email);
		verify(tokenRepository, never()).findTokenByAccountId(anyInt());
		verify(tokenRepository, never()).save(any());
		verify(mailService, never()).sendActiveAccountLink(anyString(), anyString());
	}

	@Test
	public void testResendActiveAccountWithAccountNotFound() {
		// Arrange
		String email = "example@example.com";

		when(accountRepository.findByEmail(email)).thenReturn(Optional.empty());

		// Act and Assert
		assertThrows(BadRequestException.class, () -> authenticationService.resendActiveAccount(email));
		verify(tokenRepository, never()).findTokenByAccountId(anyInt());
		verify(tokenRepository, never()).save(any());
		verify(mailService, never()).sendActiveAccountLink(anyString(), anyString());
	}

	@Test
	public void testResendActiveAccountWithValidEmail() {
		// Test data
		String email = "camnguyentqnk@gmail.com";
		Token token = Token.builder().accountToken("test").build();
		Account account = Account.builder().id(1).token(token).build();

		// Mocking accountRepository
		Mockito.when(accountRepository.findByEmail(email)).thenReturn(Optional.of(account));

		// Mocking tokenRepository
		Mockito.when(tokenRepository.findTokenByAccountId(account.getId())).thenReturn(token);

		// Test the method
		ResponseMessage result = authenticationService.resendActiveAccount(email);

		// Assert the result
		assertEquals("Success", result.getMessage());

		// Verify that the methods were called with the correct arguments
		Mockito.verify(accountRepository).findByEmail(email);
		Mockito.verify(tokenRepository).findTokenByAccountId(account.getId());
		Mockito.verify(tokenRepository).save(token);
		Mockito.verify(mailService).sendActiveAccountLink(email,
				"http://localhost:3000/verify-email/" + token.getAccountToken());
	}

}