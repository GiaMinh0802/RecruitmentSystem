package com.fpt.recruitmentsystem.service;


import java.util.List;

import com.fpt.recruitmentsystem.dto.LevelDTO;

public interface ILevelService {
    List<LevelDTO> getAll();
    LevelDTO insert(LevelDTO newLevel);
    LevelDTO update(int id, LevelDTO updatedLevel);
}

