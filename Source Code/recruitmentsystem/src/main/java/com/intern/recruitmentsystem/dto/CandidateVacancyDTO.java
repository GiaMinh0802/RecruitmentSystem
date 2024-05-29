package com.fpt.recruitmentsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandidateVacancyDTO {
    private CandidateDTO candidate;
    private VacancyDTO vacancy;
    private Date applyDate;
    private String status;
    private Integer cvId;
}
