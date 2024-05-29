package com.fpt.recruitmentsystem.dto;

import com.fpt.recruitmentsystem.enumeration.InterviewStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterviewCompactDTO {
    private Integer interviewId;
    private LocalDateTime interviewDatetime;
    private String venue;
    private String linkMeet;
    private Float interviewerScore;
    private Float softSkillScore;
    private Float languageSkillScore;
    private Float totalScore;
    private String summary;
    private InterviewStatus status;
    private InterviewerDTO interviewer;
    private RecruiterDTO recruiter;
}
