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

import com.fpt.recruitmentsystem.model.Skill;
import com.fpt.recruitmentsystem.dto.SkillDTO;
import com.fpt.recruitmentsystem.exception.ConflictException;
import com.fpt.recruitmentsystem.exception.NotFoundException;
import com.fpt.recruitmentsystem.mapper.SkillMapper;
import com.fpt.recruitmentsystem.repository.SkillRepository;
import com.fpt.recruitmentsystem.service.implement.SkillService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class SkillServiceTest {

    // Mock repositories and service
    @Mock
    private SkillRepository skillRepository;

    @Mock
    private SkillMapper skillMapper;

    @InjectMocks
    private SkillService skillService;

    private Skill createExistingSkill() {
        return Skill.builder().id(1).name("C++").build();
    }

    private SkillDTO createIncomingSkillDTO() {
        SkillDTO skillDTO = new SkillDTO();
        skillDTO.setName("Java");
        return skillDTO;
    }

    private void configMapperToDTO() {
        // Configure the map method to return a SkillDTO based on the Skill input argument
        when(skillMapper.mapToDTO(any(Skill.class))).thenAnswer(invocation -> {
            Skill sourceSkill = invocation.getArgument(0);
            SkillDTO skillDTO = new SkillDTO();
            skillDTO.setName(sourceSkill.getName());
            return skillDTO;
        });
    }

    private void configMapperToEntity() {
        // Configure the map method to return a Skill based on the SkillDTO input argument
        when(skillMapper.mapToEntity(any(SkillDTO.class))).thenAnswer(invocation -> {
            SkillDTO sourceSkillDTO = invocation.getArgument(0);
            Skill skill = Skill.builder().name(sourceSkillDTO.getName()).build();
            return skill;
        });
    }

    private void configRepoSave() {
        // Configure save method to return exactly what passed in
        when(skillRepository.save(any(Skill.class))).thenAnswer(invocation -> invocation.getArgument(0));        
    }

    @DisplayName("Test for getAll method when the skill list is not empty")
    @Test
    public void testGetAllSkillsNotEmpty() {
        Skill existingSkill = createExistingSkill();

        // Create a non-empty list of skills
        List<Skill> skillsList = Collections.singletonList(existingSkill);

        // Configure the mock to return a non-empty list
        when(skillRepository.findAll()).thenReturn(skillsList);

        configMapperToDTO();

        // Call the method to be tested
        List<SkillDTO> skills = skillService.getAll();

        // Verify that the repository's findAll method is called once
        verify(skillRepository, times(1)).findAll();

        // Verify the returned list
        assertEquals(1, skills.size(), "There should be exactly one skill in the list");
        SkillDTO skillDTO = skills.get(0);
        assertEquals(existingSkill.getName(), skillDTO.getName(), "Skill name should be equal");
    }

    @DisplayName("Test for getAll method when the skill list is empty")
    @Test
    public void testGetAllSkillsEmpty() {
        // Configure the mock to return an empty list
        when(skillRepository.findAll()).thenReturn(Collections.emptyList());

        // Call the method to be tested and expect NotFoundException
        assertThrows(NotFoundException.class, () -> skillService.getAll());

        // Verify that the repository's findAll method is called
        verify(skillRepository).findAll();
    }

    @DisplayName("Test insert new skill")
    @Test
    public void testInsertNewSkill() {
        SkillDTO incomingSkillDTO = createIncomingSkillDTO();
        configMapperToEntity();
        configMapperToDTO();
        configRepoSave();

        // Call the method to be tested
        SkillDTO insertedSkill = skillService.insert(incomingSkillDTO);

        // Assert that the returned DTO is the same as the inserted skill
        assertEquals(incomingSkillDTO.getName(), insertedSkill.getName(), "Skill name should be equal");
    }

    @DisplayName("Test update existing skill")
    @Test
    public void testUpdateExistingSkill() {
        Skill existingSkill = createExistingSkill();
        SkillDTO incomingSkillDTO = createIncomingSkillDTO();

        // Configure the mock to return the existing skill as an Optional when findById method is called
        when(skillRepository.findById(existingSkill.getId())).thenReturn(Optional.of(existingSkill));

        configMapperToDTO();
        configRepoSave();

        // Call the method to be tested
        SkillDTO updatedSkillDTOReturned = skillService.update(existingSkill.getId(), incomingSkillDTO);

        // Verify that the repository's findById method is called once with the correct ID
        verify(skillRepository, times(1)).findById(existingSkill.getId());

        // Assert that the result of the update is not null
        assertNotNull(updatedSkillDTOReturned, "Updated skill should not be null");

        // Assert that the name of the updated Skill matches the name in the updated DTO
        assertEquals(incomingSkillDTO.getName(), updatedSkillDTOReturned.getName(), "Skill name should be updated");
    }

    @DisplayName("Test update non-existing skill")
    @Test
    public void testUpdateNonExistingSkill() {
        SkillDTO incomingSkillDTO = createIncomingSkillDTO();
        final int randomId = ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE);
    
        // Call the method to be tested and expect NotFoundException
        assertThrows(NotFoundException.class, () -> skillService.update(randomId, incomingSkillDTO));
    
        // Verify that the repository's findById method is called once with the correct ID
        verify(skillRepository, times(1)).findById(randomId);
    }  

    @DisplayName("Test insert new skill with conflicting name")
    @Test
    public void testInsertSkillWithConflictingName() {
        SkillDTO incomingSkillDTO = createIncomingSkillDTO();

        // Create a Skill based on the DTO
        Skill newSkill = Skill.builder().name(incomingSkillDTO.getName()).build();

        // Configure the mock to return an existing skill with the same name
        when(skillRepository.findSkillByName(incomingSkillDTO.getName())).thenReturn(newSkill);

        // Call the method to be tested and expect ConflictException
        assertThrows(ConflictException.class, () -> skillService.insert(incomingSkillDTO));

        // Verify that the repository's findSkillByName method is called once with the name
        verify(skillRepository, times(1)).findSkillByName(incomingSkillDTO.getName());
    }

    @DisplayName("Test update existing skill with conflicting name")
    @Test
    public void testUpdateSkillWithConflictingName() {
        Skill existingSkill = createExistingSkill();
        SkillDTO incomingSkillDTO = createIncomingSkillDTO();

        final int targetId = existingSkill.getId();
        final String newName = incomingSkillDTO.getName();

        // Create an existing skill with a different ID and the conflicting name
        Skill existingSkillWithConflictingName = Skill.builder().id(targetId + 1)
                .name(newName).build();

        // Configure the mock to return the existing skill with the conflicting name
        when(skillRepository.findSkillByName(newName)).thenReturn(existingSkillWithConflictingName);

        // Configure the mock to return the existing skill with the desired id
        when(skillRepository.findById(existingSkill.getId())).thenReturn(Optional.of(existingSkill));

        // Call the method to be tested and expect ConflictException
        assertThrows(ConflictException.class, () -> skillService.update(targetId, incomingSkillDTO));

        // Verify that the repository's findSkillByName method is called once with the name
        verify(skillRepository, times(1)).findSkillByName(incomingSkillDTO.getName());
    }

    @DisplayName("Test insert new skill with leading and trailing spaces in name")
    @Test
    public void testInsertNewSkillWithLeadingAndTrailingSpaces() {
        // Create a SkillDTO with leading and trailing spaces in the name
        SkillDTO skillWithSpaces = new SkillDTO();
        skillWithSpaces.setName("  English  ");

        configMapperToDTO();
        configMapperToEntity();
        configRepoSave();

        // Call the method to be tested
        SkillDTO insertedSkill = skillService.insert(skillWithSpaces);

        // Assert that the returned DTO is the same as the inserted skill with trimmed name
        assertEquals("English", insertedSkill.getName(), "Skill name should be equal");
    }

    @DisplayName("Test update existing skill with leading and trailing spaces in name")
    @Test
    public void testUpdateExistingSkillWithLeadingAndTrailingSpaces() {
        Skill existingSkill = createExistingSkill();

        // Create a SkillDTO with leading and trailing spaces in the name
        SkillDTO skillWithSpaces = new SkillDTO();
        skillWithSpaces.setName("  English  ");

        configMapperToDTO();
        configRepoSave();
        
        // Configure the mock to return the existing skill as an Optional when findById method is called
        when(skillRepository.findById(existingSkill.getId())).thenReturn(Optional.of(existingSkill));

        // Call the method to be tested
        SkillDTO updatedSkillDTOReturned = skillService.update(existingSkill.getId(), skillWithSpaces);

        // Verify that the repository's findById method is called once with the correct ID
        verify(skillRepository, times(1)).findById(existingSkill.getId());

        // Assert that the result of the update is not null
        assertNotNull(updatedSkillDTOReturned, "Updated skill should not be null");

        // Assert that the name of the updated Skill matches the name in the updated DTO with trimmed name
        assertEquals("English", updatedSkillDTOReturned.getName(), "Skill name should be updated");
    }

}
