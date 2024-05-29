package com.fpt.recruitmentsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "blacklist")
public class Blacklist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Date startDate;
    private Date endDate;
    private String reason;

    @ManyToOne
    @JoinColumn(name = "candidate_id", referencedColumnName = "id")
    private Candidate candidate;
}
