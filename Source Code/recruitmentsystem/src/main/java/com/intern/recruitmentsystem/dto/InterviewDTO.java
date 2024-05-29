package com.fpt.recruitmentsystem.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

import com.fpt.recruitmentsystem.enumeration.InterviewStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterviewDTO {
    private Integer id;
    private LocalDateTime interviewDatetime;
    private String venue;
    private String linkMeet;
    private Float interviewerScore;
    private Float softSkillScore;
    private Float languageSkillScore;
    private Float totalScore;
    private String summary;
    private InterviewStatus status;

    private CandidateVacancyDTO candidateVacancy;
    private InterviewerDTO interviewer;
    private RecruiterDTO recruiter;
    private Set<InterviewQuestionDTO> interviewQuestion;
}
