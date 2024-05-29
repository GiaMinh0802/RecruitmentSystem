package com.fpt.recruitmentsystem.repository;

import com.fpt.recruitmentsystem.dto.report.*;
import com.fpt.recruitmentsystem.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Integer> {
    @Query("SELECT r FROM Report r WHERE r.recruiter.id = :recruiterId")
    List<Report> findAllReportByRecruiterId(int recruiterId);
    Report findReportById (int id);

    @Query(value = "SELECT count(id) as totalCandidate " +
            "FROM candidate ", nativeQuery = true)
    int totalCandidate();
    @Query(value = "SELECT count(i.id) as totalInterviewer " +
            "FROM interviewer i", nativeQuery = true)
    int totalInterviewer();
    @Query(value = "SELECT count(r.id) as totalRecruiter " +
            "FROM recruiter r", nativeQuery = true)
    int totalRecruiter();
    @Query(value = "SELECT count(b.id) as totalCandidateInBlacklist " +
            "FROM blacklist b " +
            "WHERE b.start_date >= :startDate AND b.end_date<=:endDate", nativeQuery = true)
    int totalCandidateInBlacklistByDate(Date startDate, Date endDate);
    @Query(value = "SELECT count(c.id) as totalCandidates, v.description as vacancyDescriptions " +
            "FROM vacancy v " +
            "INNER JOIN candidate_vacancy cv on v.id = cv.vacancy_id " +
            "INNER JOIN candidate c on cv.candidate_id = c.id " +
            "GROUP BY cv.vacancy_id " +
            "ORDER BY totalCandidates DESC " +
            "LIMIT 5", nativeQuery = true)
    List<ITotalCandidateVacancyDTO> totalCandidateVacancy();
    @Query(value = "SELECT MAX(iq.score) as scores, q.content as contents " +
            "FROM question q " +
            "INNER JOIN interview_question iq on q.id = iq.question_id " +
            "WHERE iq.score IS NOT NULL " +
            "GROUP BY iq.question_id ", nativeQuery = true)
    List<IQuestionByScoreDTO> questionByScore();
    @Query(value = "SELECT count(ec.collect_candidate_id) as collectedCandidates, ec.event_id as events " +
            "FROM event_collectedcandidate ec " +
            "GROUP BY ec.event_id " +
            "ORDER BY collectedCandidates DESC ", nativeQuery = true)
    List<ICCandidateByEventDTO> candidateByEvent();

    @Query(value = "SELECT count(candidate_id) as totalCandidate, status " +
            "FROM candidate_vacancy " +
            "WHERE status IS NOT NULL " +
            "GROUP BY status " +
            "ORDER BY totalCandidate DESC ", nativeQuery = true)
    List<ITotalCandidateByStatusDTO> candidateByStatus();
    @Query(value = "SELECT count(id) as interviewByMonthAndYear " +
            "FROM interview " +
            "WHERE month(interview_datetime)=:month AND year(interview_datetime)=:year", nativeQuery = true)
    int interviewByMonthAndYear(int month, int year);
}
