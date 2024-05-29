package com.fpt.recruitmentsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDateTime;

import com.fpt.recruitmentsystem.enumeration.InterviewStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VacancyInfoDTO {
    private VacancyDTO vacancy;
    private Date applyDate;
    private Integer cvId;
    private String linkCv;
    private LocalDateTime interviewDatetime;
    private InterviewStatus status;

}
