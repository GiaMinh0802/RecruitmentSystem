package com.fpt.recruitmentsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestFillterDTO {
    private List<SkillDTO> skill;
    private List<LevelDTO> level;
    private int positionId;
}
