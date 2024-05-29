package com.fpt.recruitmentsystem.service;

import com.fpt.recruitmentsystem.dto.RecruiterDTO;

public interface IRecruiterService {
    RecruiterDTO update(RecruiterDTO newProfileDto, String authorization);
    RecruiterDTO getRecruiter(String authorization);
}
