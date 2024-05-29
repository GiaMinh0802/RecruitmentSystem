package com.fpt.recruitmentsystem.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

import com.fpt.recruitmentsystem.enumeration.InterviewStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "interview")
public class Interview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDateTime interviewDatetime;
    private String venue;
    private String linkMeet;
    private Float interviewerScore;
    private Float softSkillScore;
    private Float languageSkillScore;
    private Float totalScore;
    private String summary;
    private Boolean interviewerStatus;
    private Boolean recruiterStatus;
    private InterviewStatus status;

    @OneToOne
        @JoinColumn(name = "vacancy_id", referencedColumnName = "vacancy_id")
    @JoinColumn(name = "candidate_id", referencedColumnName = "candidate_id")
    private CandidateVacancy candidateVacancy;

    @ManyToOne
    @JoinColumn(name = "interviewer_id", referencedColumnName = "id")
    private Interviewer interviewer;

    @ManyToOne
    @JoinColumn(name = "recruiter_id", referencedColumnName = "id")
    private Recruiter recruiter;

    @OneToMany(mappedBy = "interview")
    private Set<InterviewQuestion> interviewQuestions;
}
