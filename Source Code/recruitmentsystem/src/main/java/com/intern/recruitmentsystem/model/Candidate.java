package com.fpt.recruitmentsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "candidate")
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Integer sex;
    private String linkAvt;
    private Date birthday;

    @OneToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;

    @OneToMany(mappedBy = "candidate")
    private Set<Blacklist> blacklists;

    @OneToMany(mappedBy = "candidate")
    private Set<CV> cvs;

    @OneToMany(mappedBy = "candidate")
    private Set<CandidateVacancy> candidateVacancies;

    @ManyToOne
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;
}
