package com.fpt.recruitmentsystem.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "recruiter")
public class Recruiter {
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

    @OneToMany(mappedBy = "recruiter")
    private Set<Report> reports;

    @OneToMany(mappedBy = "recruiter")
    private Set<Vacancy> vacancies;

    @OneToMany(mappedBy = "recruiter")
    private Set<Event> events;

    @OneToMany(mappedBy = "recruiter")
    private Set<Interview> interviews;

    @ManyToOne
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;
}
