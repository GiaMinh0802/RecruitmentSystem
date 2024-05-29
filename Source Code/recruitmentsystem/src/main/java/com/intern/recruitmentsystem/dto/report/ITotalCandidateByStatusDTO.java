package com.fpt.recruitmentsystem.dto.report;

public interface ITotalCandidateByStatusDTO {
    int getTotalCandidate();

    String getStatus();

    void setTotalCandidate(int totalCandidate);

    void setStatus(String status);
}
