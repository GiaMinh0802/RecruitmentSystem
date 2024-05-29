package com.fpt.recruitmentsystem.service;

import java.util.List;

import com.fpt.recruitmentsystem.dto.InterviewerDTO;

public interface IInterviewerService {
     InterviewerDTO updateInterviewer(InterviewerDTO interviewerDTO, String authorizationHeader);
     InterviewerDTO getInterviewer(String authorizationHeader);
     InterviewerDTO getById(Integer id);
     List<InterviewerDTO> search(Integer page, Integer limit);
}
