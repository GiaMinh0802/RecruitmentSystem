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
@Table(name = "collectedcandidate")
public class CollectedCandidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private Integer sex;
    private Date birthday;

    @ManyToMany(mappedBy = "eventCollectedCandidate")
    private Set<Event> events;
}
