package com.fpt.recruitmentsystem.service.implement;

import com.fpt.recruitmentsystem.model.*;
import com.fpt.recruitmentsystem.service.IAccountService;
import com.fpt.recruitmentsystem.service.IAddressService;
import com.fpt.recruitmentsystem.service.IInterviewerService;
import com.fpt.recruitmentsystem.util.Message;
import com.fpt.recruitmentsystem.util.Utility;
import com.fpt.recruitmentsystem.dto.AddressDTO;
import com.fpt.recruitmentsystem.dto.InterviewerDTO;
import com.fpt.recruitmentsystem.exception.NotFoundException;
import com.fpt.recruitmentsystem.mapper.InterviewerMapper;
import com.fpt.recruitmentsystem.repository.InterviewerRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InterviewerService implements IInterviewerService {
    private final IAddressService addressService;
    private final IAccountService accountService;
    private final InterviewerRepository interviewerRepository;
    private final InterviewerMapper interviewerMapper;

    public InterviewerDTO updateInterviewer(InterviewerDTO interviewerDTO, String authorization) {
        Interviewer existingInterviewer = accountService.getInterviewerByAuthorizationHeader(authorization);

		// Copy properties from interviewerDTO to existingInterviewer EXCEPT for id
		BeanUtils.copyProperties(interviewerDTO, existingInterviewer, "id");

        AddressDTO interviewerAddressdDto = interviewerDTO.getAddress();
        Address newInterviewerAddress = (interviewerAddressdDto != null) ? addressService.findOrInsert(interviewerAddressdDto) : null;

        existingInterviewer.setAddress(newInterviewerAddress);
        Interviewer updatedInterviewer = interviewerRepository.save(existingInterviewer);

        return interviewerMapper.mapToDTO(updatedInterviewer);
    }

    public InterviewerDTO getInterviewer(String authorizationHeader) {
        Interviewer interviewer = accountService.getInterviewerByAuthorizationHeader(authorizationHeader);
        return interviewerMapper.mapToDTO(interviewer);
    }

    public InterviewerDTO getById(Integer id) {
        Interviewer interviewer = interviewerRepository.findInterviewerById(id);
        if (interviewer == null) {
            throw new NotFoundException(Message.INTERVIEWER_NOT_FOUND);
        }
        return interviewerMapper.mapToDTO(interviewer);
    }
    public List<InterviewerDTO> search(Integer page, Integer limit) {
        Pageable pageable = Utility.getPageable(page, limit);
        Page<Interviewer> interviewers = interviewerRepository.findInterviewerByIdAndAccountInactive(pageable);
        if (interviewers.isEmpty()) {
            throw new NotFoundException(Message.INTERVIEWER_NOT_FOUND);
        }
        return interviewers.stream().
                map(interviewerMapper::mapToDTO).toList();
    }
}
