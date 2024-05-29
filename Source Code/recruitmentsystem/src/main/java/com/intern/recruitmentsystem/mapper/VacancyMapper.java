package com.fpt.recruitmentsystem.mapper;

import com.fpt.recruitmentsystem.model.*;
import com.fpt.recruitmentsystem.dto.*;
import com.fpt.recruitmentsystem.repository.*;

import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class VacancyMapper {
    private final ModelMapper modelMapper;
    private final SkillRepository skillRepository;
    private final LevelRepository levelRepository;
    private final RecruiterRepository recruiterRepository;
    private final PositionRepository positionRepository;

    public Vacancy mapToEntity(VacancyDTO vacancyDTO){
        Vacancy vacancy = modelMapper.map(vacancyDTO, Vacancy.class);

        Recruiter recruiter = recruiterRepository.findRecruiterById(vacancyDTO.getRecruiter().getId());
        vacancy.setRecruiter(recruiter);


        Position position = positionRepository.findPositionById(vacancyDTO.getPosition().getId());
        vacancy.setPosition(position);

        vacancy.setVacancySkill(getSkillListEntity(vacancyDTO));
        vacancy.setVacancyLevel(getLevelListEntity(vacancyDTO));
        return vacancy;
    }

    public VacancyDTO mapToDTO(Vacancy vacancy) {
        VacancyDTO vacancyDTO = modelMapper.map(vacancy, VacancyDTO.class);

        RecruiterDTO recruiterDTO = vacancyDTO.getRecruiter();
        recruiterDTO.setEmail(vacancy.getRecruiter().getAccount().getEmail()); // Set the email from the Account entity

        vacancyDTO.setRecruiter(recruiterDTO);

        return vacancyDTO;
    }

    public VacancyInfoDTO mapToVacancyInfoDTO(Vacancy vacancy, CandidateVacancy candidateVacancy, CV cv, Interview interview) {
        VacancyInfoDTO vacancyInfoDTO = new VacancyInfoDTO();

        VacancyDTO vacancyDTO = modelMapper.map(vacancy, VacancyDTO.class);
        RecruiterDTO recruiterDTO = vacancyDTO.getRecruiter();
        recruiterDTO.setEmail(vacancy.getRecruiter().getAccount().getEmail()); // Set the email from the Account entity
        vacancyDTO.setRecruiter(recruiterDTO);
        vacancyInfoDTO.setVacancy(vacancyDTO);

        CandidateVacancyDTO candidateVacancyDTO = modelMapper.map(candidateVacancy, CandidateVacancyDTO.class);
        vacancyInfoDTO.setApplyDate(candidateVacancyDTO.getApplyDate());
        vacancyInfoDTO.setCvId(candidateVacancyDTO.getCvId());
        vacancyInfoDTO.setLinkCv(cv.getLinkCV());


        if(interview == null){
            vacancyInfoDTO.setStatus(null);
            vacancyInfoDTO.setInterviewDatetime(null);
        }
        else{
            vacancyInfoDTO.setStatus(interview.getStatus());
            vacancyInfoDTO.setInterviewDatetime(interview.getInterviewDatetime());
        }

        return vacancyInfoDTO;
    }

    public Set<Skill> getSkillListEntity(VacancyDTO vacancyDTO){
        List<Skill> skillList = new ArrayList<>();
        for(SkillDTO skillDTO: vacancyDTO.getSkill()){
            Skill skill = skillRepository.findSkillById(skillDTO.getId());
            skill.setId(skill.getId());
            skill.setName(skill.getName());
            skillList.add(skill);
        }
        return new HashSet<>(skillList);
    }
    public Set<Level> getLevelListEntity(VacancyDTO vacancyDTO){
        List<Level> levelList = new ArrayList<>();
        for (LevelDTO levelDTO: vacancyDTO.getLevel()){
            Level level = levelRepository.findLevelById(levelDTO.getId());
            level.setId(level.getId());
            level.setName(level.getName());
            levelList.add(level);
        }
        return new HashSet<>(levelList);
    }
}
