package com.fpt.recruitmentsystem.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CandidateVacancyKey implements Serializable {
    private Integer candidateId;
    private Integer vacancyId;
}
