package com.fpt.recruitmentsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VacancyDTO {
    private Integer id;
    private String benefit;
    private String description;
    private Date startDate;
    private Date endDate;
    private String referenceInformation;
    private String requirements;
    private Float salary;
    private String status;
    private Integer totalNeeded;
    private Integer remainingNeeded;
    private String workingLocation;
    private RecruiterDTO recruiter;
    private PositionDTO position;
    private Set<SkillDTO> skill;
    private Set<LevelDTO> level;
}
