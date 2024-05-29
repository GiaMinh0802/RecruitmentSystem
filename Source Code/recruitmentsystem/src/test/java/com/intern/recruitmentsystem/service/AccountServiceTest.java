package com.fpt.recruitmentsystem.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import static org.mockito.Mockito.*;

import com.fpt.recruitmentsystem.dto.AccountDTO;
import com.fpt.recruitmentsystem.exception.NotFoundException;
import com.fpt.recruitmentsystem.mapper.AccountMapper;
import com.fpt.recruitmentsystem.model.Account;
import com.fpt.recruitmentsystem.model.Admin;
import com.fpt.recruitmentsystem.model.Candidate;
import com.fpt.recruitmentsystem.model.Interviewer;
import com.fpt.recruitmentsystem.model.Recruiter;
import com.fpt.recruitmentsystem.repository.AccountRepository;
import com.fpt.recruitmentsystem.service.implement.AccountService;
import com.fpt.recruitmentsystem.util.Utility;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {
	private AccountRepository accountRepository;
	private AccountMapper accountMapper;
	private IJwtService jwtService;
	private AccountService accountService;

	@BeforeEach
	public void setup() {
		accountRepository = mock(AccountRepository.class);
		accountMapper = mock(AccountMapper.class);
		jwtService = mock(IJwtService.class);
		accountService = new AccountService(accountRepository, accountMapper, jwtService);
	}

	@Test
	void testGetAllAccounts() {
		// Create a list of mock accounts
		List<Account> accountList = new ArrayList<>();
		Account account1 = mock(Account.class);
		Account account2 = mock(Account.class);
		accountList.add(account1);
		accountList.add(account2);

		// Mock the Page and accountRepository
		Pageable pageable = Utility.getPageable(1, 10);
		Page<Account> accountsPage = new PageImpl<>(accountList, pageable, accountList.size());

		when(accountRepository.findAll(pageable)).thenReturn(accountsPage);

		AccountDTO accountDTO1 = new AccountDTO();
		AccountDTO accountDTO2 = new AccountDTO();
		when(accountMapper.mapToDTO(account1)).thenReturn(accountDTO1);
		when(accountMapper.mapToDTO(account2)).thenReturn(accountDTO2);

		// Call the service method
		List<AccountDTO> result = accountService.getAll(1, 10);

		// Assertions
		assertFalse(result.isEmpty());
		assertEquals(2, result.size());
		assertEquals(accountDTO1, result.get(0));
		assertEquals(accountDTO2, result.get(1));
	}

	@Test
	void testGetAllAccountsEmpty() {
		// Mocking the repository response
		List<Account> accountList = new ArrayList<>();
		Pageable pageable = Utility.getPageable(1, 10);
		Page<Account> accounts = new PageImpl<>(accountList, pageable, accountList.size());
		when(accountRepository.findAll(pageable)).thenReturn(accounts);

		// Call the service method
		NotFoundException exception = assertThrows(NotFoundException.class, () -> accountService.getAll(1, 10));

		assertEquals("Account Not Found", exception.getMessage());
	}

	@Test
	void testGetAccountsByRole() {
		// Mocking the repository response
		String roleName = "INTERVIEWER";
		List<Account> accounts = Arrays.asList(mock(Account.class), mock(Account.class));
		when(accountRepository.findAllAccountsforrole(roleName)).thenReturn(accounts);

		// Mocking the mapper response
		AccountDTO accountDTO1 = new AccountDTO();
		AccountDTO accountDTO2 = new AccountDTO();
		when(accountMapper.mapToDTO(accounts.get(0))).thenReturn(accountDTO1);
		when(accountMapper.mapToDTO(accounts.get(1))).thenReturn(accountDTO2);

		// Call the service method
		List<AccountDTO> result = accountService.getAccountsByRole(roleName);

		// Assertions
		assertFalse(result.isEmpty());
		assertEquals(2, result.size());
		assertEquals(accountDTO1, result.get(0));
		assertEquals(accountDTO2, result.get(1));
	}

	@Test
	void testGetAccountsByRoleEmpty() {
		// Mocking the repository response
		String roleName = "INTERVIEWER";
		when(accountRepository.findAllAccountsforrole(roleName)).thenReturn(Collections.emptyList());
		assertThrows(NotFoundException.class, () -> accountService.getAccountsByRole(roleName));
	}

	@Test
	void testGetAccountByEmail() {
		// Mocking the repository response
		String email = "test@example.com";
		Account account = mock(Account.class);
		when(accountRepository.findByEmail(email)).thenReturn(Optional.of(account));

		// Mocking the mapper response
		AccountDTO accountDTO = new AccountDTO();
		when(accountMapper.mapToDTO(account)).thenReturn(accountDTO);

		// Call the service method
		AccountDTO result = accountService.getAccountByEmail(email);

		// Assertions
		assertNotNull(result);
		assertEquals(accountDTO, result);
	}

	@Test
	void testGetAccountByEmailNotFound() {
		// Mocking the repository response
		String email = "test@example.com";
		when(accountRepository.findByEmail(email)).thenReturn(Optional.empty());

		// Call the service method
		assertThrows(NotFoundException.class, () -> accountService.getAccountByEmail(email));
	}

	@Test
	void testGetInterviewerByAuthorizationHeader() {
		// Mocking the JWT and repository responses
		String authorizationHeader = "Bearer some_jwt_token";
		String email = "test@example.com";
		Interviewer interviewer = mock(Interviewer.class);
		Account account = mock(Account.class);

		when(jwtService.extractUsername("some_jwt_token")).thenReturn(email);
		when(accountRepository.findByEmail(email)).thenReturn(Optional.of(account));
		when(account.getInterviewer()).thenReturn(interviewer);

		// Call the service method
		Interviewer result = accountService.getInterviewerByAuthorizationHeader(authorizationHeader);

		// Assertions
		assertNotNull(result);
		assertEquals(interviewer, result);
	}

	@Test
	void testGetInterviewerByAuthorizationHeaderNotFound() {
		// Mocking the JWT response
		String authorizationHeader = "Bearer some_jwt_token";

		when(jwtService.extractUsername("some_jwt_token")).thenReturn("non_existent_email");

		// Call the service method
		assertThrows(NotFoundException.class,
				() -> accountService.getInterviewerByAuthorizationHeader(authorizationHeader));
	}

	@Test
	void testGetCandidateByAuthorizationHeader() {
		// Mocking the JWT and repository responses
		String authorizationHeader = "Bearer some_jwt_token";
		String email = "test@example.com";
		Candidate candidate = mock(Candidate.class);
		Account account = mock(Account.class);

		when(jwtService.extractUsername("some_jwt_token")).thenReturn(email);
		when(accountRepository.findByEmail(email)).thenReturn(Optional.of(account));
		when(account.getCandidate()).thenReturn(candidate);

		// Call the service method
		Candidate result = accountService.getCandidateByAuthorizationHeader(authorizationHeader);

		// Assertions
		assertNotNull(result);
		assertEquals(candidate, result);
	}

	@Test
	void testGetCandidateByAuthorizationHeaderNotFound() {
		// Mocking the JWT response
		String authorizationHeader = "Bearer some_jwt_token";

		when(jwtService.extractUsername("some_jwt_token")).thenReturn("non_existent_email");

		// Call the service method
		assertThrows(NotFoundException.class,
				() -> accountService.getCandidateByAuthorizationHeader(authorizationHeader));
	}

	@Test
	void testGetRecruiterByAuthorizationHeader() {
		// Mocking the JWT and repository responses
		String authorizationHeader = "Bearer some_jwt_token";
		String email = "test@example.com";
		Recruiter recruiter = mock(Recruiter.class);
		Account account = mock(Account.class);

		when(jwtService.extractUsername("some_jwt_token")).thenReturn(email);
		when(accountRepository.findByEmail(email)).thenReturn(Optional.of(account));
		when(account.getRecruiter()).thenReturn(recruiter);

		// Call the service method
		Recruiter result = accountService.getRecruiterByAuthorizationHeader(authorizationHeader);

		// Assertions
		assertNotNull(result);
		assertEquals(recruiter, result);
	}

	@Test
	void testGetRecruiterByAuthorizationHeaderNotFound() {
		// Mocking the JWT response
		String authorizationHeader = "Bearer some_jwt_token";

		when(jwtService.extractUsername("some_jwt_token")).thenReturn("non_existent_email");

		// Call the service method
		assertThrows(NotFoundException.class,
				() -> accountService.getRecruiterByAuthorizationHeader(authorizationHeader));
	}

	@Test
	void testGetAdminByAuthorizationHeader() {
		// Mocking the JWT and repository responses
		String authorizationHeader = "Bearer some_jwt_token";
		String email = "test@example.com";
		Admin admin = mock(Admin.class);
		Account account = mock(Account.class);

		when(jwtService.extractUsername("some_jwt_token")).thenReturn(email);
		when(accountRepository.findByEmail(email)).thenReturn(Optional.of(account));
		when(account.getAdmin()).thenReturn(admin);

		// Call the service method
		Admin result = accountService.getAdminByAuthorizationHeader(authorizationHeader);

		// Assertions
		assertNotNull(result);
		assertEquals(admin, result);
	}

	@Test
	void testGetAdminByAuthorizationHeaderNotFound() {
		// Mocking the JWT response
		String authorizationHeader = "Bearer some_jwt_token";

		when(jwtService.extractUsername("some_jwt_token")).thenReturn("non_existent_email");

		// Call the service method
		assertThrows(NotFoundException.class, () -> accountService.getAdminByAuthorizationHeader(authorizationHeader));
	}

}