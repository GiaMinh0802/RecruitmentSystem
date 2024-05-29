package com.fpt.recruitmentsystem.mapper;

import com.fpt.recruitmentsystem.model.Skill;
import com.fpt.recruitmentsystem.dto.SkillDTO;

import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SkillMapper {
    private final ModelMapper modelMapper;

    public Skill mapToEntity(SkillDTO skillDTO) {
        return modelMapper.map(skillDTO, Skill.class);
    }
    public SkillDTO mapToDTO(Skill skill) {
        return modelMapper.map(skill, SkillDTO.class);
    }
}
