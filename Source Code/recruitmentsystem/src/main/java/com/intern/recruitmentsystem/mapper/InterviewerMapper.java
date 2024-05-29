package com.fpt.recruitmentsystem.mapper;

import com.fpt.recruitmentsystem.model.Account;
import com.fpt.recruitmentsystem.model.Interviewer;
import com.fpt.recruitmentsystem.dto.InterviewerDTO;

import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InterviewerMapper {
    private final ModelMapper modelMapper;
    private final AddressMapper addressMapper;

    public InterviewerDTO mapToDTO(Interviewer interviewer) {
        InterviewerDTO interviewerDTO = modelMapper.map(interviewer, InterviewerDTO.class);
        interviewerDTO.setEmail(interviewer.getAccount().getEmail()); // Set the email from the Account entity
        if (interviewer.getAddress() != null)
            interviewerDTO.setAddress(addressMapper.mapToDTO(interviewer.getAddress()));
        return interviewerDTO;
    }

    public Interviewer mapToEntity(InterviewerDTO interviewerDTO) {
        Interviewer interviewer = modelMapper.map(interviewerDTO, Interviewer.class);
        // Set the Account and Address entities from the corresponding DTOs
        interviewer.setAccount(Account.builder().email(interviewerDTO.getEmail()).build());
        if (interviewerDTO.getAddress() != null)
            interviewer.setAddress(addressMapper.mapToEntity(interviewerDTO.getAddress()));
        return interviewer;
    }
}
