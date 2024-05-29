package com.fpt.recruitmentsystem.mapper;

import com.fpt.recruitmentsystem.model.Level;
import com.fpt.recruitmentsystem.dto.LevelDTO;

import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LevelMapper {
    private final ModelMapper modelMapper;

    public Level mapToEntity(LevelDTO levelDTO) {
        return modelMapper.map(levelDTO, Level.class);
    }
    public LevelDTO mapToDTO(Level level) {
        return modelMapper.map(level, LevelDTO.class);
    }
}
