package com.fpt.recruitmentsystem.mapper;


import com.fpt.recruitmentsystem.model.Blacklist;

import lombok.RequiredArgsConstructor;

import com.fpt.recruitmentsystem.dto.BlacklistDTO;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BlacklistMapper {
    private final ModelMapper modelMapper;

    public Blacklist mapToEntity(BlacklistDTO blacklistDTO){
        return modelMapper.map(blacklistDTO, Blacklist.class);
    }
    public BlacklistDTO mapToDTO(Blacklist blacklist) {
        return modelMapper.map(blacklist, BlacklistDTO.class);
    }
}
