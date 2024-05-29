package com.fpt.recruitmentsystem.mapper;

import com.fpt.recruitmentsystem.model.Recruiter;
import com.fpt.recruitmentsystem.model.Report;
import com.fpt.recruitmentsystem.dto.report.ReportDTO;
import com.fpt.recruitmentsystem.repository.RecruiterRepository;

import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReportMapper {
    private final ModelMapper modelMapper;
    private final RecruiterRepository recruiterRepository;

    public Report mapToEntity(ReportDTO reportDTO){
        Report report = modelMapper.map(reportDTO, Report.class);
        Recruiter recruiter = recruiterRepository.findRecruiterById(reportDTO.getRecruiter().getId());
        report.setRecruiter(recruiter);
        return report;
    }
    public ReportDTO mapToDTO(Report report){
        return modelMapper.map(report, ReportDTO.class);
    }
}
