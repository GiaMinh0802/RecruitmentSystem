package com.fpt.recruitmentsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "cv")
public class CV {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Date creatdDate;
    private String fileName;
    private String linkCV;
    @Column(columnDefinition = "LONGTEXT")
    private String data;

    @ManyToOne
    @JoinColumn(name = "candidate_id", referencedColumnName = "id")
    private Candidate candidate;
}
