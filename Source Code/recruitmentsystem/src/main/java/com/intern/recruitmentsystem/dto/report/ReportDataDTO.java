package com.fpt.recruitmentsystem.dto.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportDataDTO {
    private int totalCandidate;
    private int totalInterviewer;
    private int totalRecruiter;
    private int totalCandidateInBlacklist;
    private List<ITotalCandidateVacancyDTO> totalCandidateVacancy;
    private List<IQuestionByScoreDTO> questionByScore;
    private List<ICCandidateByEventDTO> cCandidateByEvent;
    private List<ITotalCandidateByStatusDTO> totalCandidateByStatus;
    private int interviewByMonthAndYear;
}
