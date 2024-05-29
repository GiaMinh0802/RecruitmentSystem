package com.fpt.recruitmentsystem.dto.report;


public interface ITotalCandidateVacancyDTO {
    int getTotalCandidates();

    String getVacancyDescriptions();

    void setTotalCandidates(int totalCandidates);

    void setVacancyDescriptions(String vacancyDescriptions);
}
