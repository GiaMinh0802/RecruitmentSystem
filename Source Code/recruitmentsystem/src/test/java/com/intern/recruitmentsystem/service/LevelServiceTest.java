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

import com.fpt.recruitmentsystem.model.Level;
import com.fpt.recruitmentsystem.dto.LevelDTO;
import com.fpt.recruitmentsystem.exception.ConflictException;
import com.fpt.recruitmentsystem.exception.NotFoundException;
import com.fpt.recruitmentsystem.mapper.LevelMapper;
import com.fpt.recruitmentsystem.repository.LevelRepository;
import com.fpt.recruitmentsystem.service.implement.LevelService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class LevelServiceTest {

    // Mock repositories and service
    @Mock
    private LevelRepository levelRepository;

    @Mock
    private LevelMapper levelMapper;

    @InjectMocks
    private LevelService levelService;

    private Level createExistingLevel() {
        return Level.builder().id(1).name("Intern").build();
    }

    private LevelDTO createIncomingLevelDTO() {
        LevelDTO levelDTO = new LevelDTO();
        levelDTO.setName("Fresher");
        return levelDTO;
    }

    private void configMapperToDTO() {
        // Configure the map method to return a LevelDTO based on the Level input argument
        when(levelMapper.mapToDTO(any(Level.class))).thenAnswer(invocation -> {
            Level sourceLevel = invocation.getArgument(0);
            LevelDTO levelDTO = new LevelDTO();
            levelDTO.setName(sourceLevel.getName());
            return levelDTO;
        });
    }

    private void configMapperToEntity() {
        // Configure the map method to return a Level based on the LevelDTO input argument
        when(levelMapper.mapToEntity(any(LevelDTO.class))).thenAnswer(invocation -> {
            LevelDTO sourceLevelDTO = invocation.getArgument(0);
            Level level = Level.builder().name(sourceLevelDTO.getName()).build();
            return level;
        });
    }

    private void configRepoSave() {
        // Configure save method to return exactly what passed in
        when(levelRepository.save(any(Level.class))).thenAnswer(invocation -> invocation.getArgument(0));        
    }

    @DisplayName("Test for getAll method when the level list is not empty")
    @Test
    public void testGetAllLevelsNotEmpty() {
        Level existingLevel = createExistingLevel();

        // Create a non-empty list of levels
        List<Level> levelsList = Collections.singletonList(existingLevel);

        // Configure the mock to return a non-empty list
        when(levelRepository.findAll()).thenReturn(levelsList);

        configMapperToDTO();

        // Call the method to be tested
        List<LevelDTO> levels = levelService.getAll();

        // Verify that the repository's findAll method is called once
        verify(levelRepository, times(1)).findAll();

        // Verify the returned list
        assertEquals(1, levels.size(), "There should be exactly one level in the list");
        LevelDTO levelDTO = levels.get(0);
        assertEquals(existingLevel.getName(), levelDTO.getName(), "Level name should be equal");
    }

    @DisplayName("Test for getAll method when the level list is empty")
    @Test
    public void testGetAllLevelsEmpty() {
        // Configure the mock to return an empty list
        when(levelRepository.findAll()).thenReturn(Collections.emptyList());

        // Call the method to be tested and expect NotFoundException
        assertThrows(NotFoundException.class, () -> levelService.getAll());

        // Verify that the repository's findAll method is called
        verify(levelRepository).findAll();
    }

    @DisplayName("Test insert new level")
    @Test
    public void testInsertNewLevel() {
        configMapperToEntity();
        configMapperToDTO();
        configRepoSave();

        LevelDTO incomingLevelDTO = createIncomingLevelDTO();
        // Call the method to be tested
        LevelDTO insertedLevel = levelService.insert(incomingLevelDTO);

        // Assert that the returned DTO is the same as the inserted level
        assertEquals(incomingLevelDTO.getName(), insertedLevel.getName(), "Level name should be equal");
    }

    @DisplayName("Test update existing level")
    @Test
    public void testUpdateExistingLevel() {
        Level existingLevel = createExistingLevel();
        LevelDTO incomingLevelDTO = createIncomingLevelDTO();
        
        // Configure the mock to return the existing level as an Optional when findById method is called
        when(levelRepository.findById(existingLevel.getId())).thenReturn(Optional.of(existingLevel));

        configMapperToDTO();
        configRepoSave();

        // Call the method to be tested
        LevelDTO updatedLevelDTOReturned = levelService.update(existingLevel.getId(), incomingLevelDTO);

        // Verify that the repository's findById method is called once with the correct ID
        verify(levelRepository, times(1)).findById(existingLevel.getId());

        // Assert that the result of the update is not null
        assertNotNull(updatedLevelDTOReturned, "Updated level should not be null");

        // Assert that the name of the updated Level matches the name in the updated DTO
        assertEquals(incomingLevelDTO.getName(), updatedLevelDTOReturned.getName(), "Level name should be updated");
    }

    @DisplayName("Test update non-existing level")
    @Test
    public void testUpdateNonExistingLevel() {
        LevelDTO incomingLevelDTO = createIncomingLevelDTO();
        final int randomId = ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE);
    
        // Call the method to be tested and expect NotFoundException
        assertThrows(NotFoundException.class, () -> levelService.update(randomId, incomingLevelDTO));
    
        // Verify that the repository's findById method is called once with the correct ID
        verify(levelRepository, times(1)).findById(randomId);
    }    

    @DisplayName("Test insert new level with conflicting name")
    @Test
    public void testInsertLevelWithConflictingName() {
        LevelDTO incomingLevelDTO = createIncomingLevelDTO();

        // Create a Level based on the DTO
        Level newLevel = Level.builder().name(incomingLevelDTO.getName()).build();

        // Configure the mock to return an existing level with the same name
        when(levelRepository.findLevelByName(incomingLevelDTO.getName())).thenReturn(newLevel);

        // Call the method to be tested and expect ConflictException
        assertThrows(ConflictException.class, () -> levelService.insert(incomingLevelDTO));

        // Verify that the repository's findLevelByName method is called once with the name
        verify(levelRepository, times(1)).findLevelByName(incomingLevelDTO.getName());
    }

    @DisplayName("Test update existing level with conflicting name")
    @Test
    public void testUpdateLevelWithConflictingName() {
        Level existingLevel = createExistingLevel();
        LevelDTO incomingLevelDTO = createIncomingLevelDTO();

        final int targetId = existingLevel.getId();
        final String newName = incomingLevelDTO.getName();

        // Create an existing level with a different ID and the conflicting name
        Level existingLevelWithConflictingName = Level.builder().id(targetId + 1)
                .name(newName).build();

        // Configure the mock to return the existing level with the conflicting name
        when(levelRepository.findLevelByName(newName)).thenReturn(existingLevelWithConflictingName);

        // Configure the mock to return the existing level with the desired id
        when(levelRepository.findById(existingLevel.getId())).thenReturn(Optional.of(existingLevel));

        // Call the method to be tested and expect ConflictException
        assertThrows(ConflictException.class, () -> levelService.update(targetId, incomingLevelDTO));

        // Verify that the repository's findLevelByName method is called once with the name
        verify(levelRepository, times(1)).findLevelByName(incomingLevelDTO.getName());
    }

    @DisplayName("Test insert new level with leading and trailing spaces in name")
    @Test
    public void testInsertNewLevelWithLeadingAndTrailingSpaces() {
        // Create a LevelDTO with leading and trailing spaces in the name
        LevelDTO levelWithSpaces = new LevelDTO();
        levelWithSpaces.setName("  Senior  ");

        configMapperToDTO();
        configMapperToEntity();
        configRepoSave();

        // Call the method to be tested
        LevelDTO insertedLevel = levelService.insert(levelWithSpaces);

        // Assert that the returned DTO is the same as the inserted level with trimmed name
        assertEquals("Senior", insertedLevel.getName(), "Level name should be equal");
    }

    @DisplayName("Test update existing level with leading and trailing spaces in name")
    @Test
    public void testUpdateExistingLevelWithLeadingAndTrailingSpaces() {
        Level existingLevel = createExistingLevel();

        // Create a LevelDTO with leading and trailing spaces in the name
        LevelDTO levelWithSpaces = new LevelDTO();
        levelWithSpaces.setName("  Senior  ");

        configMapperToDTO();
        configRepoSave();
        
        // Configure the mock to return the existing level as an Optional when findById method is called
        when(levelRepository.findById(existingLevel.getId())).thenReturn(Optional.of(existingLevel));

        // Call the method to be tested
        LevelDTO updatedLevelDTOReturned = levelService.update(existingLevel.getId(), levelWithSpaces);

        // Verify that the repository's findById method is called once with the correct ID
        verify(levelRepository, times(1)).findById(existingLevel.getId());

        // Assert that the result of the update is not null
        assertNotNull(updatedLevelDTOReturned, "Updated level should not be null");

        // Assert that the name of the updated Level matches the name in the updated DTO with trimmed name
        assertEquals("Senior", updatedLevelDTOReturned.getName(), "Level name should be updated");
    }

}
