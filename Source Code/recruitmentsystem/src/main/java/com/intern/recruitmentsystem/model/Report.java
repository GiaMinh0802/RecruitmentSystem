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
@Table(name = "report")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private Date createAt;
    @Column(columnDefinition = "LONGTEXT")
    private String description;
    private String summary;

    @ManyToOne
    @JoinColumn(name = "recruiter_id", referencedColumnName = "id")
    private Recruiter recruiter;
}
