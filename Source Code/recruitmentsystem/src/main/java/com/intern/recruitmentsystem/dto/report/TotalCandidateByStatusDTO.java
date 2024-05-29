package com.fpt.recruitmentsystem.dto.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TotalCandidateByStatusDTO implements ITotalCandidateByStatusDTO {
    private int totalCandidate;
    private String status;
}
