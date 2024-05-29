package com.fpt.recruitmentsystem.mapper;

import com.fpt.recruitmentsystem.model.Account;
import com.fpt.recruitmentsystem.model.Recruiter;
import com.fpt.recruitmentsystem.dto.RecruiterDTO;

import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RecruiterMapper {
    private final ModelMapper modelMapper;
    private final AddressMapper addressMapper;

    public RecruiterDTO mapToDTO(Recruiter recruiter) {
        RecruiterDTO recruiterDTO = modelMapper.map(recruiter, RecruiterDTO.class);
        recruiterDTO.setEmail(recruiter.getAccount().getEmail()); // Set the email from the Account entity
        if (recruiter.getAddress() != null)
            recruiterDTO.setAddress(addressMapper.mapToDTO(recruiter.getAddress()));
        return recruiterDTO;
    }

    public Recruiter mapToEntity(RecruiterDTO recruiterDTO) {
        Recruiter recruiter = modelMapper.map(recruiterDTO, Recruiter.class);
        // Set the Account and Address entities from the corresponding DTOs
        recruiter.setAccount(Account.builder().email(recruiterDTO.getEmail()).build());
        if (recruiterDTO.getAddress() != null)
            recruiter.setAddress(addressMapper.mapToEntity(recruiterDTO.getAddress()));
        return recruiter;
    }
}
