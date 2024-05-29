package com.fpt.recruitmentsystem.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fpt.recruitmentsystem.model.Position;
import com.fpt.recruitmentsystem.dto.PositionDTO;
import com.fpt.recruitmentsystem.exception.ConflictException;
import com.fpt.recruitmentsystem.exception.NotFoundException;
import com.fpt.recruitmentsystem.mapper.PositionMapper;
import com.fpt.recruitmentsystem.repository.PositionRepository;
import com.fpt.recruitmentsystem.service.implement.PositionService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class PositionServiceTest {

    // Mock repositories and service
    @Mock
    private PositionRepository positionRepository;

    @Mock
    private PositionMapper positionMapper;

    @InjectMocks
    private PositionService positionService;

    private Position createExistingPosition() {
        return Position.builder().id(1).name("Front-end").build();
    }

    private PositionDTO createIncomingPositionDTO() {
        PositionDTO positionDTO = new PositionDTO();
        positionDTO.setName("Back-end");
        return positionDTO;
    }

    private void configMapperToDTO() {
        // Configure the map method to return a PositionDTO based on the Position input argument
        when(positionMapper.mapToDTO(any(Position.class))).thenAnswer(invocation -> {
            Position sourcePosition = invocation.getArgument(0);
            PositionDTO positionDTO = new PositionDTO();
            positionDTO.setName(sourcePosition.getName());
            return positionDTO;
        });
    }

    private void configMapperToEntity() {
        // Configure the map method to return a Position based on the PositionDTO input argument
        when(positionMapper.mapToEntity(any(PositionDTO.class))).thenAnswer(invocation -> {
            PositionDTO sourcePositionDTO = invocation.getArgument(0);
            Position position = Position.builder().name(sourcePositionDTO.getName()).build();
            return position;
        });
    }

    private void configRepoSave() {
        // Configure save method to return exactly what passed in
        when(positionRepository.save(any(Position.class))).thenAnswer(invocation -> invocation.getArgument(0));        
    }

    @DisplayName("Test for getAll method when the position list is not empty")
    @Test
    public void testGetAllPositionsNotEmpty() {
        Position existingPosition = createExistingPosition();

        // Create a non-empty list of positions
        List<Position> positionsList = Collections.singletonList(existingPosition);

        // Configure the mock to return a non-empty list
        when(positionRepository.findAll()).thenReturn(positionsList);

        configMapperToDTO();

        // Call the method to be tested
        List<PositionDTO> positions = positionService.getAll();

        // Verify that the repository's findAll method is called once
        verify(positionRepository, times(1)).findAll();

        // Verify the returned list
        assertEquals(1, positions.size(), "There should be exactly one position in the list");
        PositionDTO positionDTO = positions.get(0);
        assertEquals(existingPosition.getName(), positionDTO.getName(), "Position name should be equal");
    }

    @DisplayName("Test for getAll method when the position list is empty")
    @Test
    public void testGetAllPositionsEmpty() {
        // Configure the mock to return an empty list
        when(positionRepository.findAll()).thenReturn(Collections.emptyList());

        // Call the method to be tested and expect NotFoundException
        assertThrows(NotFoundException.class, () -> positionService.getAll());

        // Verify that the repository's findAll method is called
        verify(positionRepository).findAll();
    }

    @DisplayName("Test insert new position")
    @Test
    public void testInsertNewPosition() {
        PositionDTO incomingPositionDTO = createIncomingPositionDTO();
        configMapperToEntity();
        configMapperToDTO();
        configRepoSave();

        // Call the method to be tested
        PositionDTO insertedPosition = positionService.insert(incomingPositionDTO);

        // Assert that the returned DTO is the same as the inserted position
        assertEquals(incomingPositionDTO.getName(), insertedPosition.getName(), "Position name should be equal");
    }

    @DisplayName("Test update existing position")
    @Test
    public void testUpdateExistingPosition() {
        Position existingPosition = createExistingPosition();
        PositionDTO incomingPositionDTO = createIncomingPositionDTO();

        // Configure the mock to return the existing position as an Optional when findById method is called
        when(positionRepository.findById(existingPosition.getId())).thenReturn(Optional.of(existingPosition));

        configMapperToDTO();
        configRepoSave();

        // Call the method to be tested
        PositionDTO updatedPositionDTOReturned = positionService.update(existingPosition.getId(), incomingPositionDTO);

        // Verify that the repository's findById method is called once with the correct ID
        verify(positionRepository, times(1)).findById(existingPosition.getId());

        // Assert that the result of the update is not null
        assertNotNull(updatedPositionDTOReturned, "Updated position should not be null");

        // Assert that the name of the updated Position matches the name in the updated DTO
        assertEquals(incomingPositionDTO.getName(), updatedPositionDTOReturned.getName(), "Position name should be updated");
    }

    @DisplayName("Test update non-existing position")
    @Test
    public void testUpdateNonExistingPosition() {
        PositionDTO incomingPositionDTO = createIncomingPositionDTO();
        final int randomId = ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE);
    
        // Call the method to be tested and expect NotFoundException
        assertThrows(NotFoundException.class, () -> positionService.update(randomId, incomingPositionDTO));
    
        // Verify that the repository's findById method is called once with the correct ID
        verify(positionRepository, times(1)).findById(randomId);
    }  

    @DisplayName("Test insert new position with conflicting name")
    @Test
    public void testInsertPositionWithConflictingName() {
        PositionDTO incomingPositionDTO = createIncomingPositionDTO();

        // Create a Position based on the DTO
        Position newPosition = Position.builder().name(incomingPositionDTO.getName()).build();

        // Configure the mock to return an existing position with the same name
        when(positionRepository.findPositionByName(incomingPositionDTO.getName())).thenReturn(newPosition);

        // Call the method to be tested and expect ConflictException
        assertThrows(ConflictException.class, () -> positionService.insert(incomingPositionDTO));

        // Verify that the repository's findPositionByName method is called once with the name
        verify(positionRepository, times(1)).findPositionByName(incomingPositionDTO.getName());
    }

    @DisplayName("Test update existing position with conflicting name")
    @Test
    public void testUpdatePositionWithConflictingName() {
        Position existingPosition = createExistingPosition();
        PositionDTO incomingPositionDTO = createIncomingPositionDTO();

        final int targetId = existingPosition.getId();
        final String newName = incomingPositionDTO.getName();

        // Create an existing position with a different ID and the conflicting name
        Position existingPositionWithConflictingName = Position.builder().id(targetId + 1)
                .name(newName).build();

        // Configure the mock to return the existing position with the conflicting name
        when(positionRepository.findPositionByName(newName)).thenReturn(existingPositionWithConflictingName);

        // Configure the mock to return the existing position with the desired id
        when(positionRepository.findById(existingPosition.getId())).thenReturn(Optional.of(existingPosition));

        // Call the method to be tested and expect ConflictException
        assertThrows(ConflictException.class, () -> positionService.update(targetId, incomingPositionDTO));

        // Verify that the repository's findPositionByName method is called once with the name
        verify(positionRepository, times(1)).findPositionByName(incomingPositionDTO.getName());
    }

    @DisplayName("Test insert new position with leading and trailing spaces in name")
    @Test
    public void testInsertNewPositionWithLeadingAndTrailingSpaces() {
        // Create a PositionDTO with leading and trailing spaces in the name
        PositionDTO positionWithSpaces = new PositionDTO();
        positionWithSpaces.setName("  DevOps  ");

        configMapperToDTO();
        configMapperToEntity();
        configRepoSave();

        // Call the method to be tested
        PositionDTO insertedPosition = positionService.insert(positionWithSpaces);

        // Assert that the returned DTO is the same as the inserted position with trimmed name
        assertEquals("DevOps", insertedPosition.getName(), "Position name should be equal");
    }

    @DisplayName("Test update existing position with leading and trailing spaces in name")
    @Test
    public void testUpdateExistingPositionWithLeadingAndTrailingSpaces() {
        Position existingPosition = createExistingPosition();

        // Create a PositionDTO with leading and trailing spaces in the name
        PositionDTO positionWithSpaces = new PositionDTO();
        positionWithSpaces.setName("  DevOps  ");

        configMapperToDTO();
        configRepoSave();

        // Configure the mock to return the existing position as an Optional when findById method is called
        when(positionRepository.findById(existingPosition.getId())).thenReturn(Optional.of(existingPosition));

        // Call the method to be tested
        PositionDTO updatedPositionDTOReturned = positionService.update(existingPosition.getId(), positionWithSpaces);

        // Verify that the repository's findById method is called once with the correct ID
        verify(positionRepository, times(1)).findById(existingPosition.getId());

        // Assert that the result of the update is not null
        assertNotNull(updatedPositionDTOReturned, "Updated position should not be null");

        // Assert that the name of the updated Position matches the name in the updated DTO with trimmed name
        assertEquals("DevOps", updatedPositionDTOReturned.getName(), "Position name should be updated");
    }
}
