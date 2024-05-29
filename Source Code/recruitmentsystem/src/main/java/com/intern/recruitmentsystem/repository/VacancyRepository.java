package com.fpt.recruitmentsystem.repository;

import com.fpt.recruitmentsystem.model.Candidate;
import com.fpt.recruitmentsystem.model.Vacancy;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VacancyRepository extends JpaRepository<Vacancy, Integer> {
    public List<Vacancy> findAll(Specification<Vacancy> spec, Pageable pageable);
    public int count(Specification<Vacancy> spec);
    public Vacancy findVacancyById(Integer id);

    @Query(value = "SELECT v.id FROM vacancy v  join vacancy_skill vs on vs.vacancy_id = v.id " +
            "where vs.skill_id IN (:skillIdList)", nativeQuery = true)
    public Set<Integer> findVacancyBySkillIdList (List<Integer> skillIdList);

    @Query(value = "SELECT v.id FROM vacancy v  join vacancy_level vs on vs.vacancy_id = v.id " +
            "where vs.level_id IN (:levelIdList) ", nativeQuery = true)
    public Set<Integer> findVacancyByLevelIdList (List<Integer> levelIdList);

    @Query(value = "SELECT v.id FROM vacancy v  where v.position_id=:positionId ", nativeQuery = true)
    public Set<Integer> findVacancyByPositionId (Integer positionId);

    @Query(value = "SELECT c from CandidateVacancy cv join Candidate c on cv.candidate.id = c.id where cv.vacancy.id IN (:vacancyList)")
    public List<Candidate> findCandidateByVacancyList(List<Integer> vacancyList);


    @Query(value = "SELECT v.id FROM vacancy v join vacancy_level vs on vs.vacancy_id = v.id " +
            "join vacancy_skill vskill on vskill.vacancy_id = v.id where vs.level_id IN (:levelIdList) and vskill.skill_id IN (:skillIdList) and v.position_id=:positionId ", nativeQuery = true)
    public Set<Integer> findVacancyByAll (List<Integer> levelIdList, int positionId, List<Integer> skillIdList);

    @Query(value = "SELECT c FROM CandidateVacancy cv JOIN Candidate c ON cv.candidate.id = c.id WHERE cv.vacancy.id = :vacancyId")
    public List<Candidate> findListCandidateByVacancyId(@Param("vacancyId") Integer vacancyId);

    @Query(value = "SELECT v FROM Vacancy v JOIN Recruiter r ON v.recruiter.id = r.id WHERE v.id = :vacancyId")
    public Vacancy findRecruiterById(Integer vacancyId);
}
