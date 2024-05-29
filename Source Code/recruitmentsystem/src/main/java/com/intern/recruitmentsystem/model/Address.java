package com.fpt.recruitmentsystem.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "address",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"country", "city", "street"})
        })
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String country;
    private String city;
    private String street;

    @OneToMany(mappedBy = "address")
    private Set<Candidate> candidates;

    @OneToMany(mappedBy = "address")
    private Set<Interviewer> interviewers;

    @OneToMany(mappedBy = "address")
    private Set<Admin> admins;

    @OneToMany(mappedBy = "address")
    private Set<Recruiter> recruiters;
}
