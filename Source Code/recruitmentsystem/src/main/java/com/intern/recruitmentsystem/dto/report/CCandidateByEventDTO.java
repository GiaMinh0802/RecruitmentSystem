package com.fpt.recruitmentsystem.dto.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CCandidateByEventDTO implements ICCandidateByEventDTO {
    private int collectedCandidates;
    private int events;
}
