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
@Table(name = "interviewer")
public class Interviewer {
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

    @OneToMany(mappedBy = "interviewer")
    private Set<Interview> interviews;

    @ManyToOne
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;
}
