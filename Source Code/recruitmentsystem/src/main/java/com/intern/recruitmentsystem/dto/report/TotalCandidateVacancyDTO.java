package com.fpt.recruitmentsystem.dto.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TotalCandidateVacancyDTO implements ITotalCandidateVacancyDTO {
    private int totalCandidates;
    private String vacancyDescriptions;
}
