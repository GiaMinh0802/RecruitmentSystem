package com.fpt.recruitmentsystem.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "candidate_vacancy")
public class CandidateVacancy {
    @EmbeddedId
    private CandidateVacancyKey id;
    private Date applyDate;
    private String status;
    private Integer cvId;

    @ManyToOne
    @MapsId("vacancyId")
    @JoinColumn(name = "vacancy_id", referencedColumnName = "id")
    private Vacancy vacancy;

    @ManyToOne
    @MapsId("candidateId")
    @JoinColumn(name = "candidate_id", referencedColumnName = "id")
    private Candidate candidate;

    @OneToOne(mappedBy = "candidateVacancy")
    private Interview interview;

}
