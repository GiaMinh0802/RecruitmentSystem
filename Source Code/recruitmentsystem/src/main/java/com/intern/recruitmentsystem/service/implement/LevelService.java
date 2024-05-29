package com.fpt.recruitmentsystem.service.implement;

import java.util.List;

import com.fpt.recruitmentsystem.service.ILevelService;
import com.fpt.recruitmentsystem.dto.LevelDTO;
import com.fpt.recruitmentsystem.exception.ConflictException;
import com.fpt.recruitmentsystem.exception.NotFoundException;
import com.fpt.recruitmentsystem.mapper.LevelMapper;
import com.fpt.recruitmentsystem.repository.LevelRepository;
import com.fpt.recruitmentsystem.util.Message;
import com.fpt.recruitmentsystem.util.Utility;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.fpt.recruitmentsystem.model.Level;


@Service
@RequiredArgsConstructor
public class LevelService implements ILevelService {
    private final LevelRepository levelRepository;
    private final LevelMapper levelMapper;

    public List<LevelDTO> getAll() {
        List<Level> levels = levelRepository.findAll();
        if (levels.isEmpty()) {
            throw new NotFoundException(Message.LEVEL_NOT_FOUND);
        }
        return levels.stream()
                .map(levelMapper::mapToDTO)
                .toList();
    }

    public LevelDTO insert(LevelDTO newLevel) {
        String trimmedName = Utility.trimString(newLevel.getName());
        Level existingLevel = levelRepository.findLevelByName(trimmedName);
        if (existingLevel != null) {
            throw new ConflictException("Level name already exists");
        }

        Level level = levelMapper.mapToEntity(newLevel);
        level.setName(trimmedName); // Set the trimmed name before saving
        Level savedLevel = levelRepository.save(level);
        return levelMapper.mapToDTO(savedLevel);
    }

    public LevelDTO update(int id, LevelDTO updatedLevel) {
        Level level = levelRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Message.LEVEL_NOT_FOUND));

        String trimmedName = Utility.trimString(updatedLevel.getName());
        Level existingLevel = levelRepository.findLevelByName(trimmedName);
        if (existingLevel != null && existingLevel.getId() != id) {
            throw new ConflictException("Level name already exists");
        }

        level.setName(trimmedName); // Set the trimmed name before updating
        Level updated = levelRepository.save(level);
        return levelMapper.mapToDTO(updated);
    }
}
