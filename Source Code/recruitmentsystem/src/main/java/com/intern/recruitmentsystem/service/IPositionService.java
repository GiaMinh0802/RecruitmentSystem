package com.fpt.recruitmentsystem.service;

import java.util.List;

import com.fpt.recruitmentsystem.dto.PositionDTO;

public interface IPositionService {
    List<PositionDTO> getAll();
    PositionDTO insert(PositionDTO newPosition);
    PositionDTO update(int id, PositionDTO updatedPosition);
}
