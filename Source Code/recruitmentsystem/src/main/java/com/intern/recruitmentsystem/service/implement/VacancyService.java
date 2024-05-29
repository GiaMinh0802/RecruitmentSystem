package com.fpt.recruitmentsystem.service.implement;

import com.fpt.recruitmentsystem.dto.*;
import com.fpt.recruitmentsystem.model.Candidate;
import com.fpt.recruitmentsystem.model.Level;
import com.fpt.recruitmentsystem.model.Skill;
import com.fpt.recruitmentsystem.model.Vacancy;
import com.fpt.recruitmentsystem.service.IVacancyService;
import com.fpt.recruitmentsystem.exception.NotFoundException;
import com.fpt.recruitmentsystem.mapper.CandidateMapper;
import com.fpt.recruitmentsystem.mapper.VacancyMapper;
import com.fpt.recruitmentsystem.repository.VacancyRepository;
import com.fpt.recruitmentsystem.util.Message;
import com.fpt.recruitmentsystem.util.Utility;

import jakarta.persistence.criteria.Join;
import lombok.AllArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import jakarta.persistence.criteria.Predicate;

import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class VacancyService implements IVacancyService {
    private VacancyRepository vacancyRepository;
    private VacancyMapper vacancyMapper;
    private CandidateMapper candidateMapper;

    @Override
    public VacancyDTO addVacancy(VacancyDTO vacancyDTO){
        Vacancy vacancyToEntity = vacancyMapper.mapToEntity(vacancyDTO);
        vacancyToEntity.setStatus("1");
        Vacancy vacancynew =  vacancyRepository.save(vacancyToEntity);
        return vacancyMapper.mapToDTO(vacancynew);
    }

    private static Specification<Vacancy> buildVacancySpecification(String positionIds, String skillIds, String levelIds, String searchText) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.equal(root.get("status"), "1"));

            query.orderBy(criteriaBuilder.desc(root.get("startDate")));

            if (positionIds != null) {
                List<Integer> positionIdList = Utility.parseIdList(positionIds);
                predicates.add(root.get("position").get("id").in(positionIdList));
            }

            if (levelIds != null) {
                List<Integer> levelIdList = Utility.parseIdList(levelIds);
                Join<Vacancy, Level> levelJoin = root.joinSet("vacancyLevel");
                predicates.add(levelJoin.get("id").in(levelIdList));
            }

            if (skillIds != null) {
                List<Integer> skillIdList = Utility.parseIdList(skillIds);
                Join<Vacancy, Skill> skillJoin = root.joinSet("vacancySkill");
                predicates.add(skillJoin.get("id").in(skillIdList));
            }

            if (searchText != null) {
                String searchPattern = "%" + searchText.toLowerCase() + "%";
                Predicate descriptionPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), searchPattern);
                Predicate requirementsPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("requirements")), searchPattern);
                Predicate locationPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("workingLocation")), searchPattern);
                predicates.add(criteriaBuilder.or(descriptionPredicate, requirementsPredicate, locationPredicate));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    @Override
    public List<VacancyDTO> search(String positionIds, String skillIds, String levelIds,
                                   String searchText, Integer page, Integer limit) {
        Specification<Vacancy> specification = buildVacancySpecification(positionIds, skillIds, levelIds, searchText);
        Pageable pageable = Utility.getPageable(page, limit);
        List<Vacancy> vacancies = vacancyRepository.findAll(specification, pageable);
        List<VacancyDTO> vacancyDtos = vacancies.stream()
                .map(vacancyMapper::mapToDTO)
                .toList();

        if (vacancyDtos.isEmpty()) {
            throw new NotFoundException(Message.VACANCY_NOT_FOUND);
        }
        return vacancyDtos;
    }

    @Override
    public Integer count(String positionIds, String skillIds, String levelIds, String searchText) {
        Specification<Vacancy> specification = buildVacancySpecification(positionIds, skillIds, levelIds, searchText);
        return vacancyRepository.count(specification);
    }

    @Override
    public VacancyDTO updateVacancy(int id, VacancyDTO vacancyDTO){
        Vacancy vacancyUpdate = vacancyRepository.findVacancyById(id);
        if (vacancyUpdate == null){
            throw new NotFoundException(Message.VACANCY_NOT_FOUND);
        }

        Vacancy vacancyToEntity = vacancyMapper.mapToEntity(vacancyDTO);

        vacancyUpdate.setStatus(vacancyToEntity.getStatus());
        vacancyUpdate.setTotalNeeded(vacancyToEntity.getTotalNeeded());
        vacancyUpdate.setStartDate(vacancyToEntity.getStartDate());
        vacancyUpdate.setEndDate(vacancyToEntity.getEndDate());
        vacancyUpdate.setDescription(vacancyToEntity.getDescription());
        vacancyUpdate.setRemainingNeeded(vacancyToEntity.getRemainingNeeded());
        vacancyUpdate.setRequirements(vacancyToEntity.getRequirements());
        vacancyUpdate.setSalary(vacancyToEntity.getSalary());
        vacancyUpdate.setBenefit(vacancyToEntity.getBenefit());
        vacancyUpdate.setReferenceInformation(vacancyToEntity.getReferenceInformation());
        vacancyUpdate.setWorkingLocation(vacancyToEntity.getWorkingLocation());

        vacancyUpdate.setRecruiter(vacancyToEntity.getRecruiter());
        vacancyUpdate.setPosition(vacancyToEntity.getPosition());
        vacancyUpdate.setVacancySkill(vacancyToEntity.getVacancySkill());
        vacancyUpdate.setVacancyLevel(vacancyToEntity.getVacancyLevel());

        Vacancy vacancyNew = vacancyRepository.save(vacancyUpdate);
        return vacancyMapper.mapToDTO(vacancyNew);
    }

    @Override
    public VacancyDTO hide(int id) {
        Vacancy vacancy = vacancyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Message.VACANCY_NOT_FOUND));
        vacancy.setStatus("0");
        Vacancy updatedVacancy = vacancyRepository.save(vacancy);
        return vacancyMapper.mapToDTO(updatedVacancy);
    }

    @Override
    public VacancyDTO unhide(int id) {
        Vacancy vacancy = vacancyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Message.VACANCY_NOT_FOUND));
        vacancy.setStatus("1");
        Vacancy updatedVacancy = vacancyRepository.save(vacancy);
        return vacancyMapper.mapToDTO(updatedVacancy);
    }

    @Override
    public Set<CandidateDTO> getCandidateBySkill(List<Integer> skillIdList){
        Set<Integer> vacancyList = vacancyRepository.findVacancyBySkillIdList(skillIdList);
        List<Candidate> candidateList = vacancyRepository.findCandidateByVacancyList(vacancyList.stream().toList());

        return candidateList.stream().map(candidate -> candidateMapper.mapToDTO(candidate)).
                collect(Collectors.toSet());
    }

    @Override
    public Set<CandidateDTO> getCandidateByLevel(List<Integer> levelIdList) {
        Set<Integer> vacancyList = vacancyRepository.findVacancyByLevelIdList(levelIdList);
        List<Candidate> candidateList = vacancyRepository.findCandidateByVacancyList(vacancyList.stream().toList());

        return candidateList.stream().map(candidate -> candidateMapper.mapToDTO(candidate)).
                collect(Collectors.toSet());
    }

    @Override
    public Set<CandidateDTO> getCandidateByPosition(int positionId) {
        Set<Integer> vacancyList = vacancyRepository.findVacancyByPositionId(positionId);
        List<Candidate> candidateList = vacancyRepository.findCandidateByVacancyList(vacancyList.stream().toList());

        return candidateList.stream().map(candidate -> candidateMapper.mapToDTO(candidate)).
                collect(Collectors.toSet());
    }

    @Override
    public Set<CandidateDTO> filterCandidateAll(List<SkillDTO> skillDTOList, List<LevelDTO> levelDTOList, int positionId){
        List<Integer> skillIdList = new ArrayList<>();
        List<Integer> levelIdList = new ArrayList<>();

        for(LevelDTO levelDTO : levelDTOList){
            levelIdList.add(levelDTO.getId());
        }

        for(SkillDTO skillDTO : skillDTOList){
            skillIdList.add(skillDTO.getId());
        }

        Set<Integer> vacancyList = vacancyRepository.findVacancyByAll(levelIdList, positionId, skillIdList);

        List<Candidate> candidateList = vacancyRepository.findCandidateByVacancyList(vacancyList.stream().toList());

        return candidateList.stream().map(candidate -> candidateMapper.mapToDTO(candidate)).
                collect(Collectors.toSet());
    }

    @Override
    public List<CandidateInterviewDTO> getListCandidateByVacancyId(int id) {
        List<Candidate> candidateList = vacancyRepository.findListCandidateByVacancyId(id);
        if (candidateList.isEmpty()) {
            throw new NotFoundException("Candidate Not Found");
        }
        return candidateList.stream().
                map(candidate -> candidateMapper.mapToCandidateInterviewDTO(candidate,id)).
                toList();
    }
    public VacancyDTO getVacancy(Integer vacancyId){
        Vacancy vacancy = vacancyRepository.findById(vacancyId)
                .orElseThrow(() -> new NotFoundException(Message.VACANCY_NOT_FOUND));

        return vacancyMapper.mapToDTO(vacancy);
    }
}
