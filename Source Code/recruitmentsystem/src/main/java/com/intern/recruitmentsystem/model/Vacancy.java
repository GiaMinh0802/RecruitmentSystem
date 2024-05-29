package com.fpt.recruitmentsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "vacancy")
public class Vacancy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String status;
    private Integer totalNeeded;
    private Integer remainingNeeded;
    private Date startDate;
    private Date endDate;
    @Column(columnDefinition = "LONGTEXT")
    private String description;
    @Column(columnDefinition = "LONGTEXT")
    private String requirements;
    private Float salary;
    @Column(columnDefinition = "LONGTEXT")
    private String benefit;
    @Column(columnDefinition = "LONGTEXT")
    private String referenceInformation;
    private String workingLocation;

    @OneToMany(mappedBy = "vacancy")
    private Set<CandidateVacancy> candidateVacancies;

    @ManyToOne
    @JoinColumn(name = "recruiter_id", referencedColumnName = "id")
    private Recruiter recruiter;

    @ManyToOne
    @JoinColumn(name = "position_id", referencedColumnName = "id")
    private Position position;

    @ManyToMany
    @JoinTable(name = "vacancy_skill",
            joinColumns = @JoinColumn(name = "vacancy_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private Set<Skill> vacancySkill;

    @ManyToMany
    @JoinTable(name = "vacancy_level",
            joinColumns = @JoinColumn(name = "vacancy_id"),
            inverseJoinColumns = @JoinColumn(name = "level_id"))
    private Set<Level> vacancyLevel;
}
