package com.fpt.recruitmentsystem.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fpt.recruitmentsystem.model.Blacklist;
import com.fpt.recruitmentsystem.model.Candidate;
import com.fpt.recruitmentsystem.dto.BlacklistDTO;
import com.fpt.recruitmentsystem.exception.BadRequestException;
import com.fpt.recruitmentsystem.exception.NotFoundException;
import com.fpt.recruitmentsystem.mapper.BlacklistMapper;
import com.fpt.recruitmentsystem.repository.BlacklistRepository;
import com.fpt.recruitmentsystem.repository.CandidateRepository;
import com.fpt.recruitmentsystem.service.implement.BlacklistService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class BlacklistServiceTest {

    @Mock
    private BlacklistRepository blacklistRepository;

    @Mock
    private CandidateRepository candidateRepository;

    @Mock
    private BlacklistMapper blacklistMapper;

    @InjectMocks
    private BlacklistService blacklistService;

    private Candidate existingCandidate;
    private Blacklist existingBlacklist;

    @BeforeEach
    public void setUp() {
        // Create sample user
        existingCandidate = Candidate.builder().
                id(1).
                firstName("John").
                lastName("Doe").build();

        // Suppose the above user has been in blacklist since 2023-07-01
        existingBlacklist = Blacklist.builder().
                id(1).
                candidate(existingCandidate).
                startDate(Date.valueOf("2023-07-01")).
                endDate(null).build();
    }

    private void configMapperToEntity() {
        // Configure the map method to return a Blacklist based on the BlacklistDTO input argument
        when(blacklistMapper.mapToEntity(any(BlacklistDTO.class))).thenAnswer(invocation -> {
            BlacklistDTO sourceDTO = invocation.getArgument(0);
            Blacklist blacklist = Blacklist.builder()
                    .startDate(sourceDTO.getStartDate())
                    .endDate(sourceDTO.getEndDate())
                    .reason(sourceDTO.getReason())
                    .build();
            return blacklist;
        });
    }

    private void configMapperToDTO() {
        // Configure the map method to return a BlacklistDTO based on the Blacklist input argument
        when(blacklistMapper.mapToDTO(any(Blacklist.class))).thenAnswer(invocation -> {
            Blacklist sourceBlacklist = invocation.getArgument(0);
            BlacklistDTO blacklistDTO = new BlacklistDTO();
            blacklistDTO.setStartDate(sourceBlacklist.getStartDate());
            blacklistDTO.setEndDate(sourceBlacklist.getEndDate());
            blacklistDTO.setReason(sourceBlacklist.getReason());
            blacklistDTO.setCandidateId(sourceBlacklist.getCandidate().getId());
            return blacklistDTO;
        });
    }


    @Test
    @DisplayName("Test isCandidateAlreadyInBlacklist with an existing blacklist")
    public void testIsCandidateAlreadyInBlacklistWithExistingBlacklist() {
        // Arrange
        when(blacklistRepository.findFirstByCandidateIdOrderByStartDateDesc(existingCandidate.getId())).thenReturn(existingBlacklist);

        // Act
        boolean result = blacklistService.isCandidateAlreadyInBlacklist(existingCandidate.getId());

        // Assert
        assertTrue(result, "The candidate should be in the blacklist");
    }

    @Test
    @DisplayName("Test isCandidateAlreadyInBlacklist with an existing blacklist and endDate set in the past")
    public void testIsCandidateAlreadyInBlacklistWithExistingBlacklistAndEndDate() {
        // Arrange
        // Set by a day in the past
        existingBlacklist.setEndDate(Date.valueOf("2023-07-15"));
        when(blacklistRepository.findFirstByCandidateIdOrderByStartDateDesc(existingCandidate.getId())).thenReturn(existingBlacklist);
        // Act
        boolean result = blacklistService.isCandidateAlreadyInBlacklist(existingCandidate.getId());

        // Assert
        assertFalse(result, "The candidate should not be in the blacklist");
    }

    @Test
    @DisplayName("Test isCandidateAlreadyInBlacklist with no blacklist")
    public void testIsCandidateAlreadyInBlacklistWithNoBlacklist() {
        // Arrange
        when(blacklistRepository.findFirstByCandidateIdOrderByStartDateDesc(existingCandidate.getId())).thenReturn(null);

        // Act
        boolean result = blacklistService.isCandidateAlreadyInBlacklist(existingCandidate.getId());

        // Assert
        assertFalse(result, "The candidate should not be in the blacklist");
    }

    @Test
    @DisplayName("Test getBlacklistHistory with existing blacklist history")
    public void testGetBlacklistHistoryWithExistingBlacklistHistory() {
        // Arrange
        when(candidateRepository.findById(existingCandidate.getId())).thenReturn(Optional.of(existingCandidate));
        when(blacklistRepository.findAllByCandidateId(existingCandidate.getId())).thenReturn(List.of(existingBlacklist));
        when(blacklistMapper.mapToDTO(existingBlacklist)).thenReturn(new BlacklistDTO());

        // Act
        List<BlacklistDTO> result = blacklistService.getBlacklistHistory(existingCandidate.getId());

        // Assert
        assertFalse(result.isEmpty(), "Blacklist history should not be empty");
    }

    @Test
    @DisplayName("Test getBlacklistHistory with no blacklist history")
    public void testGetBlacklistHistoryWithNoBlacklistHistory() {
        // Arrange
        when(candidateRepository.findById(existingCandidate.getId())).thenReturn(Optional.of(existingCandidate));
        when(blacklistRepository.findAllByCandidateId(existingCandidate.getId())).thenReturn(new ArrayList<>());
    
        // Act & Assert
        assertThrowsNotFoundException(() -> blacklistService.getBlacklistHistory(existingCandidate.getId()));
    }    

    @Test
    @DisplayName("Test insert new blacklist record for a candidate")
    public void testInsertNewBlacklistRecord() {
        // Arrange
        BlacklistDTO newBlacklistDto = new BlacklistDTO();
        newBlacklistDto.setCandidateId(existingCandidate.getId());

        configMapperToEntity();
        configMapperToDTO();
        when(candidateRepository.findById(existingCandidate.getId())).thenReturn(Optional.of(existingCandidate));
        when(blacklistRepository.findFirstByCandidateIdOrderByStartDateDesc(existingCandidate.getId())).thenReturn(null);
        when(blacklistRepository.save(any())).thenReturn(existingBlacklist);

        // Act
        BlacklistDTO insertedRecord = blacklistService.insert(existingCandidate.getId(), newBlacklistDto);
        
        // Assert
        assertNotNull(insertedRecord, "Inserted blacklist record should not be null");
    }

    @Test
    @DisplayName("Test insert new blacklist record for a candidate already in blacklist")
    public void testInsertNewBlacklistRecordForCandidateInBlacklist() {
        // Arrange
        BlacklistDTO newBlacklistDto = new BlacklistDTO();
        newBlacklistDto.setCandidateId(existingCandidate.getId());

        when(candidateRepository.findById(existingCandidate.getId())).thenReturn(Optional.of(existingCandidate));
        when(blacklistRepository.findFirstByCandidateIdOrderByStartDateDesc(existingCandidate.getId())).thenReturn(existingBlacklist);

        // Act & Assert
        assertThrowsBadRequestException(() -> blacklistService.insert(existingCandidate.getId(), newBlacklistDto));
    }

    @Test
    @DisplayName("Test unblacklist a candidate")
    public void testUnblacklistCandidate() {
        // Arrange
        configMapperToDTO();
        when(candidateRepository.findById(existingCandidate.getId())).thenReturn(Optional.of(existingCandidate));
        when(blacklistRepository.findFirstByCandidateIdOrderByStartDateDesc(existingCandidate.getId())).thenReturn(existingBlacklist);

        java.util.Date currentDate = new java.util.Date();
        when(blacklistRepository.save(any())).thenReturn(existingBlacklist);

        // Act
        BlacklistDTO updatedRecord = blacklistService.unblacklist(existingCandidate.getId());
        
        // Assert
        assertNotNull(updatedRecord, "Updated blacklist record should not be null");
        // Convert Date objects to LocalDate
        LocalDate expectedEndDate = currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate actualEndDate = updatedRecord.getEndDate().toLocalDate();

        // Use the equals method to compare LocalDate objects
        assertEquals(expectedEndDate, actualEndDate, "End date should be set to today's date");
    }

    @Test
    @DisplayName("Test unblacklist a candidate not in blacklist")
    public void testUnblacklistCandidateNotInBlacklist() {
        // Arrange
        when(candidateRepository.findById(existingCandidate.getId())).thenReturn(Optional.of(existingCandidate));
        when(blacklistRepository.findFirstByCandidateIdOrderByStartDateDesc(existingCandidate.getId())).thenReturn(null);

        // Act & Assert
        assertThrowsBadRequestException(() -> blacklistService.unblacklist(existingCandidate.getId()));
    }

    private void assertThrowsNotFoundException(Executable executable) {
        assertThrows(NotFoundException.class, executable);
    }

    private void assertThrowsBadRequestException(Executable executable) {
        assertThrows(BadRequestException.class, executable);
    }
}
