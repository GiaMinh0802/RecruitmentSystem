package com.fpt.recruitmentsystem.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fpt.recruitmentsystem.model.Recruiter;
import com.fpt.recruitmentsystem.model.Vacancy;
import com.fpt.recruitmentsystem.dto.RecruiterDTO;
import com.fpt.recruitmentsystem.dto.VacancyDTO;
import com.fpt.recruitmentsystem.mapper.VacancyMapper;
import com.fpt.recruitmentsystem.repository.VacancyRepository;
import com.fpt.recruitmentsystem.service.implement.VacancyService;
import com.fpt.recruitmentsystem.util.ResponseMessage;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

@ExtendWith(MockitoExtension.class)
public class VacancyServiceTest {
    // Mock repositories and service

    @Mock
    private VacancyRepository vacancyRepository;

    @Mock
    private VacancyMapper vacancyMapper;

    @MockBean
    private ResponseMessage responseMessage;

    @Mock
    private RecruiterDTO recruiterDTO;

    @Mock
    private Recruiter recruiter;

    @InjectMocks
    private VacancyService vacancyService;

    private Vacancy createExistingVacancy(){
        return Vacancy.builder().
                id(1).
                status("pending").
                totalNeeded(1).
                remainingNeeded(4).build();
    }

    private VacancyDTO createIncomingVacancyDTO(){
        VacancyDTO vacancyDTO = new VacancyDTO();
        vacancyDTO.setStatus("Approve");
        vacancyDTO.setTotalNeeded(1);
        vacancyDTO.setRemainingNeeded(2);

        return vacancyDTO;
    }

    private void configMapperToDTO(){
        when(vacancyMapper.mapToDTO(any(Vacancy.class))).thenAnswer(invocation -> {
            Vacancy sourceVacancy = invocation.getArgument(0);
            VacancyDTO vacancyDTO = new VacancyDTO();
            vacancyDTO.setStatus(sourceVacancy.getStatus());
            vacancyDTO.setTotalNeeded(sourceVacancy.getTotalNeeded());
            vacancyDTO.setRemainingNeeded(sourceVacancy.getRemainingNeeded());
            vacancyDTO.setRecruiter(recruiterDTO);
            return vacancyDTO;
        });
    }

    private void configMapperToEntity() {
        // Configure the map method to return a Position based on the PositionDTO input argument
        when(vacancyMapper.mapToEntity(any(VacancyDTO.class))).thenAnswer(invocation -> {
            VacancyDTO sourceVacancyDTO = invocation.getArgument(0);
            Vacancy vacancy = Vacancy.builder().status(sourceVacancyDTO.getStatus())
                    .totalNeeded(sourceVacancyDTO.getTotalNeeded())
                    .remainingNeeded(sourceVacancyDTO.getRemainingNeeded())
                    .recruiter(recruiter).build();
            return vacancy;
        });
    }

    private void configRepoSave() {
        // Configure save method to return exactly what passed in
        when(vacancyRepository.save(any(Vacancy.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @DisplayName("Test insert new vacancy")
    @Test
    public void testInsertNewVacancy() {
        VacancyDTO incomingVacancyDTO = createIncomingVacancyDTO();

        configMapperToEntity();
        configRepoSave();
        configMapperToDTO();

        // Call the method to be tested
        VacancyDTO vacancyDTOInserted = vacancyService.addVacancy(incomingVacancyDTO);

        // Assert that the returned DTO is the same as the inserted position
        assertEquals(incomingVacancyDTO.getStatus(), vacancyDTOInserted.getStatus(), "Vacancy status should be equal");
    }

    @DisplayName("Test update existing vacancy")
    @Test
    public void testUpdateExistingVacancy() {
        Vacancy existingVacancy = createExistingVacancy();
        VacancyDTO incomingVacancyDTO = createIncomingVacancyDTO();

        configMapperToEntity();

        // Configure the mock to return the existing position as an Optional when findById method is called
        when(vacancyRepository.findVacancyById(existingVacancy.getId())).thenReturn(existingVacancy);
        when(vacancyRepository.save(any(Vacancy.class))).thenReturn(existingVacancy);
        when(vacancyMapper.mapToDTO(existingVacancy)).thenReturn(incomingVacancyDTO);

        // Call the method to be tested
        VacancyDTO updatedVacancyDTOReturned = vacancyService.updateVacancy(existingVacancy.getId(), incomingVacancyDTO);

        assertEquals(incomingVacancyDTO.getStatus(), updatedVacancyDTOReturned.getStatus(), "Vacancy Status should be updated");

        // Verify that the repository's findById method is called once with the correct ID
        verify(vacancyRepository, times(1)).findVacancyById(existingVacancy.getId());
    }
}
