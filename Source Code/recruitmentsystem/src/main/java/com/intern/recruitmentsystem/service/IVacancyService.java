package com.fpt.recruitmentsystem.service;

import com.fpt.recruitmentsystem.dto.*;

import java.util.List;
import java.util.Set;

public interface IVacancyService {
    VacancyDTO addVacancy(VacancyDTO vacancyDTO);
    List<VacancyDTO> search(String positionIds, String skillIds, String levelIds,
                            String searchText, Integer page, Integer limit);
    Integer count(String positionIds, String skillIds, String levelIds, String searchText);
    VacancyDTO updateVacancy(int id, VacancyDTO vacancyDTO);
    VacancyDTO hide(int id);
    VacancyDTO unhide(int id);
    Set<CandidateDTO> filterCandidateAll(List<SkillDTO> skillDTOList, List<LevelDTO> levelDTOList, int positionId);
    Set<CandidateDTO> getCandidateBySkill(List<Integer> skillIdList);
    Set<CandidateDTO> getCandidateByLevel(List<Integer> levelIdList);
    Set<CandidateDTO> getCandidateByPosition(int positionId);
    List<CandidateInterviewDTO> getListCandidateByVacancyId(int positionId);
    VacancyDTO getVacancy(Integer vacancyId);
}
