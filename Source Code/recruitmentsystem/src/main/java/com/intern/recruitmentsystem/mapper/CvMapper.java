package com.fpt.recruitmentsystem.mapper;


import com.fpt.recruitmentsystem.model.CV;
import com.fpt.recruitmentsystem.dto.CvDTO;

import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CvMapper {
    private final ModelMapper modelMapper;

    public CV mapToEntity(CvDTO cvDTO){
		return modelMapper.map(cvDTO, CV.class);
	}
    public CvDTO mapToDTO(CV cv) {
		return modelMapper.map(cv, CvDTO.class);
	}
}
