package com.fpt.recruitmentsystem.repository;

import com.fpt.recruitmentsystem.model.Interviewer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface InterviewerRepository extends JpaRepository<Interviewer, Integer> {
    Interviewer findInterviewerById(int id);

    Interviewer findInterviewerByAccountId(Integer id);
    @Query(value="select ac.email from Interviewer i join Account ac on i.account.id = ac.id where i.id=:interviewerId")
    String getEmailInterviewr(int interviewerId);

    @Query("SELECT i FROM Interviewer i JOIN Account ac on i.account.id = ac.id WHERE ac.isActive = true")
    Page<Interviewer> findInterviewerByIdAndAccountInactive(Pageable pageable);
}
