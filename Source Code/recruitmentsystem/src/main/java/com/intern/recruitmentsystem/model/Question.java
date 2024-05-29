package com.fpt.recruitmentsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "question")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(columnDefinition = "LONGTEXT")
    private String content;
    @Column(columnDefinition = "LONGTEXT")
    private String answer;

    @ManyToOne
    @JoinColumn(name = "skill_id", referencedColumnName = "id")
    private Skill skill;

    @OneToMany(mappedBy = "question")
    private Set<InterviewQuestion> interviewQuestions;
}
