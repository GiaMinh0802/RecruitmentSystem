package com.fpt.recruitmentsystem.repository;

import com.fpt.recruitmentsystem.model.InterviewQuestion;
import com.fpt.recruitmentsystem.model.InterviewQuestionKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterviewQuesionRepository extends JpaRepository<InterviewQuestion, InterviewQuestionKey> {
    InterviewQuestion findInterviewQuestionsByInterviewIdAndQuestionId(Integer interviewerId, Integer questionId);

    List<InterviewQuestion> findAllByInterviewId(Integer interviewerId);


}
