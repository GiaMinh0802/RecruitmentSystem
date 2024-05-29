package com.fpt.recruitmentsystem.mapper;

import com.fpt.recruitmentsystem.model.Position;
import com.fpt.recruitmentsystem.dto.PositionDTO;

import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PositionMapper {
    private final ModelMapper modelMapper;

    public Position mapToEntity(PositionDTO positionDTO) {
        return modelMapper.map(positionDTO, Position.class);
    }
    public PositionDTO mapToDTO(Position position) {
        return modelMapper.map(position, PositionDTO.class);
    }
}
