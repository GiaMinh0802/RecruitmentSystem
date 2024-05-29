package com.fpt.recruitmentsystem.service.implement;

import com.fpt.recruitmentsystem.model.Address;
import com.fpt.recruitmentsystem.model.Recruiter;
import com.fpt.recruitmentsystem.service.IAccountService;
import com.fpt.recruitmentsystem.service.IAddressService;
import com.fpt.recruitmentsystem.service.IRecruiterService;
import com.fpt.recruitmentsystem.dto.AddressDTO;
import com.fpt.recruitmentsystem.dto.RecruiterDTO;
import com.fpt.recruitmentsystem.mapper.RecruiterMapper;
import com.fpt.recruitmentsystem.repository.RecruiterRepository;

import lombok.AllArgsConstructor;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RecruiterService implements IRecruiterService {
    private final IAddressService addressService;
    private final IAccountService accountService;
    private final RecruiterRepository recruiterRepository;
    private final RecruiterMapper recruiterMapper;

    public RecruiterDTO update(RecruiterDTO recruiterDTO, String authorization) {
        Recruiter existingRecruiter = accountService.getRecruiterByAuthorizationHeader(authorization);

		// Copy properties from recruiterDTO to existingRecruiter EXCEPT for id
		BeanUtils.copyProperties(recruiterDTO, existingRecruiter, "id");

        AddressDTO recruiterAddressdDto = recruiterDTO.getAddress();
        Address newRecruiterAddress = (recruiterAddressdDto != null) ? addressService.findOrInsert(recruiterAddressdDto) : null;

        existingRecruiter.setAddress(newRecruiterAddress);
        Recruiter updatedRecruiter = recruiterRepository.save(existingRecruiter);

        return recruiterMapper.mapToDTO(updatedRecruiter);
    }

    public RecruiterDTO getRecruiter(String authorization) {
        Recruiter recruiter = accountService.getRecruiterByAuthorizationHeader(authorization);
        return recruiterMapper.mapToDTO(recruiter);
    }
}
