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
@Table(name = "skill")
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;

    @ManyToMany(mappedBy = "vacancySkill")
    private Set<Vacancy> vacancies;

    @OneToMany(mappedBy = "skill")
    private Set<Question> question;
}
