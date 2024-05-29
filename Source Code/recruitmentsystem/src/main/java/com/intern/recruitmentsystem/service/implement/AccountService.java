package com.fpt.recruitmentsystem.service.implement;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fpt.recruitmentsystem.model.Account;
import com.fpt.recruitmentsystem.model.Admin;
import com.fpt.recruitmentsystem.model.Candidate;
import com.fpt.recruitmentsystem.model.Interviewer;
import com.fpt.recruitmentsystem.model.Recruiter;
import com.fpt.recruitmentsystem.service.IAccountService;
import com.fpt.recruitmentsystem.service.IJwtService;
import com.fpt.recruitmentsystem.util.Message;
import com.fpt.recruitmentsystem.util.Utility;

import com.fpt.recruitmentsystem.dto.AccountDTO;
import com.fpt.recruitmentsystem.exception.NotFoundException;
import com.fpt.recruitmentsystem.mapper.AccountMapper;
import com.fpt.recruitmentsystem.repository.AccountRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AccountService implements IAccountService{
	private final AccountRepository accountRepository;
	private final AccountMapper accountMapper;
	private final IJwtService jwtService;

	public List<AccountDTO> getAll(Integer  page,Integer limit){
		Pageable pageable = Utility.getPageable(page, limit);
		Page<Account> accounts = accountRepository.findAll(pageable);
		if (accounts.isEmpty()) {
			throw new NotFoundException(Message.ACCOUNT_NOT_FOUND);
		}
		return accounts.stream().map(accountMapper::mapToDTO)
				.toList();
	}
		

	public List<AccountDTO> getAccountsByRole(String roleName) {
	    List<Account> accounts = accountRepository.findAllAccountsforrole(roleName);

	    if (accounts.isEmpty()) {
			throw new NotFoundException(Message.ACCOUNT_NOT_FOUND);
	    }
	    return accounts.stream().map(accountMapper::mapToDTO)
	    		.toList();
	}

	public AccountDTO getAccountByEmail(String email) {
	    Account account = accountRepository.findByEmail(email)
			.orElseThrow(() -> new NotFoundException(Message.ACCOUNT_NOT_FOUND));
	    return accountMapper.mapToDTO(account);
	}

    public Interviewer getInterviewerByAuthorizationHeader(String authorizationHeader) {
        String jwt = authorizationHeader.substring(7);
        String email = jwtService.extractUsername(jwt);
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(Message.INTERVIEWER_NOT_FOUND));
        return account.getInterviewer();
    }

    public Candidate getCandidateByAuthorizationHeader(String authorizationHeader) {
        String jwt = authorizationHeader.substring(7);
        String email = jwtService.extractUsername(jwt);
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(Message.CANDIDATE_NOT_FOUND));
        return account.getCandidate();
    }

    public Recruiter getRecruiterByAuthorizationHeader(String authorizationHeader) {
        String jwt = authorizationHeader.substring(7);
        String email = jwtService.extractUsername(jwt);
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(Message.RECRUITER_NOT_FOUND));
        return account.getRecruiter();
    }

    public Admin getAdminByAuthorizationHeader(String authorizationHeader) {
        String jwt = authorizationHeader.substring(7);
        String email = jwtService.extractUsername(jwt);
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(Message.ADMIN_NOT_FOUND));
        return account.getAdmin();
    }

    public Account getAccountByAuthorizationHeader(String authorizationHeader) {
        String jwt = authorizationHeader.substring(7);
        String email = jwtService.extractUsername(jwt);
        return accountRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(Message.ACCOUNT_NOT_FOUND));
    }
}
