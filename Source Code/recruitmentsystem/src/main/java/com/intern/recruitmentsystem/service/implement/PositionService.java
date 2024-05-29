package com.fpt.recruitmentsystem.service.implement;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fpt.recruitmentsystem.model.Position;
import com.fpt.recruitmentsystem.service.IPositionService;
import com.fpt.recruitmentsystem.dto.PositionDTO;
import com.fpt.recruitmentsystem.exception.ConflictException;
import com.fpt.recruitmentsystem.exception.NotFoundException;
import com.fpt.recruitmentsystem.mapper.PositionMapper;
import com.fpt.recruitmentsystem.repository.PositionRepository;
import com.fpt.recruitmentsystem.util.Message;
import com.fpt.recruitmentsystem.util.Utility;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PositionService implements IPositionService {
    private final PositionRepository positionRepository;
    private final PositionMapper positionMapper;

    public List<PositionDTO> getAll() {
        List<Position> positions = positionRepository.findAll();
        if (positions.isEmpty()) {
            throw new NotFoundException(Message.POSITION_NOT_FOUND);
        }
        return positions.stream()
                .map(positionMapper::mapToDTO)
                .toList();
    }

    public PositionDTO insert(PositionDTO newPosition) {
        String trimmedName = Utility.trimString(newPosition.getName());
        Position existingPosition = positionRepository.findPositionByName(trimmedName);
        if (existingPosition != null) {
            throw new ConflictException("Position name already exists");
        }

        Position position = positionMapper.mapToEntity(newPosition);
        position.setName(trimmedName); // Set the trimmed name before saving
        Position savedPosition = positionRepository.save(position);
        return positionMapper.mapToDTO(savedPosition);
    }

    public PositionDTO update(int id, PositionDTO updatedPosition) {
        Position position = positionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Message.POSITION_NOT_FOUND));

        String trimmedName = Utility.trimString(updatedPosition.getName());
        Position existingPosition = positionRepository.findPositionByName(trimmedName);
        if (existingPosition != null && existingPosition.getId() != id) {
            throw new ConflictException("Position name already exists");
        }

        position.setName(trimmedName); // Set the trimmed name before updating
        Position updated = positionRepository.save(position);
        return positionMapper.mapToDTO(updated);
    }

}
