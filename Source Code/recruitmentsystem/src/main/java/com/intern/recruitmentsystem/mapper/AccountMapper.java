package com.fpt.recruitmentsystem.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.fpt.recruitmentsystem.model.Account;
import com.fpt.recruitmentsystem.dto.AccountDTO;
import com.fpt.recruitmentsystem.dto.RoleDTO;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AccountMapper {
	private final ModelMapper modelMapper;
	private final AddressMapper addressMapper;
	
	public AccountDTO mapToDTO(Account account) {
		AccountDTO accountDTO = new AccountDTO();
		accountDTO.setId(account.getId());
		accountDTO.setEmail(account.getEmail()); 
		if (account.getAdmin() != null) {
			accountDTO.setFirstName(account.getAdmin().getFirstName());
			accountDTO.setLastName(account.getAdmin().getLastName());
			accountDTO.setPhoneNumber(account.getAdmin().getPhoneNumber());
			accountDTO.setSex(account.getAdmin().getSex());
			accountDTO.setBirthday(account.getAdmin().getBirthday());
			accountDTO.setLinkAvt(account.getAdmin().getLinkAvt());
			if (account.getAdmin().getAddress() != null)
				accountDTO.setAddress(addressMapper.mapToDTO(account.getAdmin().getAddress()));
		} else if (account.getCandidate() != null) {
			accountDTO.setFirstName(account.getCandidate().getFirstName());
			accountDTO.setLastName(account.getCandidate().getLastName());
			accountDTO.setPhoneNumber(account.getCandidate().getPhoneNumber());
			accountDTO.setSex(account.getCandidate().getSex());
			accountDTO.setBirthday(account.getCandidate().getBirthday());
			accountDTO.setLinkAvt(account.getCandidate().getLinkAvt());
			if (account.getCandidate().getAddress() != null)
				accountDTO.setAddress(addressMapper.mapToDTO(account.getCandidate().getAddress()));
		} else if (account.getRecruiter() != null) {
			accountDTO.setFirstName(account.getRecruiter().getFirstName());
			accountDTO.setLastName(account.getRecruiter().getLastName());
			accountDTO.setPhoneNumber(account.getRecruiter().getPhoneNumber());
			accountDTO.setSex(account.getRecruiter().getSex());
			accountDTO.setBirthday(account.getRecruiter().getBirthday());
			accountDTO.setLinkAvt(account.getRecruiter().getLinkAvt());
			if (account.getRecruiter().getAddress() != null)
				accountDTO.setAddress(addressMapper.mapToDTO(account.getRecruiter().getAddress()));
		}
		else if (account.getInterviewer() != null) {
			accountDTO.setFirstName(account.getInterviewer().getFirstName());
			accountDTO.setLastName(account.getInterviewer().getLastName());
			accountDTO.setPhoneNumber(account.getInterviewer().getPhoneNumber());
			accountDTO.setSex(account.getInterviewer().getSex());
			accountDTO.setBirthday(account.getInterviewer().getBirthday());
			accountDTO.setLinkAvt(account.getInterviewer().getLinkAvt());
			if (account.getInterviewer().getAddress() != null)
				accountDTO.setAddress(addressMapper.mapToDTO(account.getInterviewer().getAddress()));
		}
		accountDTO.setRole(modelMapper.map(account.getRole(),RoleDTO.class));
		accountDTO.setIsActive(account.isActive());

		return accountDTO;
	}
}
