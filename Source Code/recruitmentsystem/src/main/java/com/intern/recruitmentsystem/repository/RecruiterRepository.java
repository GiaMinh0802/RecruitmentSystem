package com.fpt.recruitmentsystem.repository;

import com.fpt.recruitmentsystem.model.Recruiter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecruiterRepository extends JpaRepository<Recruiter, Integer> {
    public Recruiter findRecruiterById(int id);
    Recruiter findRecruiterByAccountId(Integer id);
    @Query(value="select ac.email from Recruiter r join Account ac on r.account.id = ac.id where r.id=:recrutierid")
    String getEmailRecruiter(int recrutierid);

    @Query(value = "select v.id from Vacancy v where v.recruiter.id=:recrutierid")
    List<Integer> getVacancyByRecruiter(int recrutierid);
}
