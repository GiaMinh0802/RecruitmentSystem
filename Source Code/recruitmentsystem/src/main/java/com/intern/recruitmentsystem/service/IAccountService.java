package com.fpt.recruitmentsystem.service;

import java.util.List;

import com.fpt.recruitmentsystem.model.Account;
import com.fpt.recruitmentsystem.model.Admin;
import com.fpt.recruitmentsystem.model.Candidate;
import com.fpt.recruitmentsystem.model.Interviewer;
import com.fpt.recruitmentsystem.model.Recruiter;
import com.fpt.recruitmentsystem.dto.*;

public interface IAccountService {
	List<AccountDTO> getAll(Integer page,Integer limit);
	List<AccountDTO> getAccountsByRole(String roleName);
	AccountDTO getAccountByEmail(String email);
	Admin getAdminByAuthorizationHeader(String authorizationHeader);
	Recruiter getRecruiterByAuthorizationHeader(String authorizationHeader);
	Interviewer getInterviewerByAuthorizationHeader(String authorizationHeader);
	Candidate getCandidateByAuthorizationHeader(String authorizationHeader);
	Account getAccountByAuthorizationHeader(String authorizationHeader);
}
