package com.fpt.recruitmentsystem.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private LocalDate createAt;
    private Date startDate;
    private Date endDate;
    private String location;

    @Column(columnDefinition = "LONGTEXT")
    private String description;

    private String status;

    @Column(columnDefinition = "LONGTEXT")
    private String rule;

    @Column(columnDefinition = "LONGTEXT")
    private String benefit;

    @ManyToOne
    @JoinColumn(name = "recruiter_id", referencedColumnName = "id")
    private Recruiter recruiter;

    @ManyToMany
    @JoinTable(name = "event_collectedcandidate",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "collect_candidate_id"))
    private Set<CollectedCandidate> eventCollectedCandidate;
}
