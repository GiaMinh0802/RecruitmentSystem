package com.fpt.recruitmentsystem.service;

import com.fpt.recruitmentsystem.model.Account;
import com.fpt.recruitmentsystem.model.CV;
import com.fpt.recruitmentsystem.model.Candidate;
import com.fpt.recruitmentsystem.model.CandidateVacancy;
import com.fpt.recruitmentsystem.model.CandidateVacancyKey;
import com.fpt.recruitmentsystem.model.Vacancy;
import com.fpt.recruitmentsystem.dto.CandidateDTO;
import com.fpt.recruitmentsystem.dto.CandidateVacancyDTO;
import com.fpt.recruitmentsystem.dto.VacancyDTO;
import com.fpt.recruitmentsystem.exception.NotFoundException;
import com.fpt.recruitmentsystem.mapper.CandidateMapper;
import com.fpt.recruitmentsystem.mapper.CandidateVacancyMapper;
import com.fpt.recruitmentsystem.repository.CVRepository;
import com.fpt.recruitmentsystem.repository.CandidateRepository;
import com.fpt.recruitmentsystem.repository.CandidateVacancyRepository;
import com.fpt.recruitmentsystem.repository.VacancyRepository;
import com.fpt.recruitmentsystem.service.implement.CandidateService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class CandidateServiceTest {

    @Mock
    private CandidateRepository candidateRepository;

    @Mock
    private CVRepository cvRepository;

    @Mock
    private VacancyRepository vacancyRepository;

    @Mock
    private CandidateVacancyRepository candidateVacancyRepository;

    @Mock
    private CandidateMapper candidateMapper;

    @Mock
    private CandidateVacancyMapper candidateVacancyMapper;

    @Mock
    private IAccountService accountService;

    @Mock
    private IBlacklistService blacklistService;

    @InjectMocks
    private CandidateService candidateService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        configCandidateMapperToEntity();
        configCandidateMapperToDTO();
    }

    private void configCandidateMapperToEntity() {
        // Configure the mapToEntity method to return a Candidate based on the CandidateDTO input argument
        when(candidateMapper.mapToEntity(any(CandidateDTO.class))).thenAnswer(invocation -> {
            CandidateDTO sourceDTO = invocation.getArgument(0);
            Candidate candidate = Candidate.builder()
                    .id(sourceDTO.getId())
                    .firstName(sourceDTO.getFirstName())
                    .lastName(sourceDTO.getLastName())
                    .phoneNumber(sourceDTO.getPhoneNumber())
                    .sex(sourceDTO.getSex())
                    .birthday(sourceDTO.getBirthday())
                    .linkAvt(sourceDTO.getLinkAvt())
                    .build();
            return candidate;
        });
    }

    private void configCandidateMapperToDTO() {
        // Configure the mapToDTO method to return a CandidateDTO based on the Candidate input argument
        when(candidateMapper.mapToDTO(any(Candidate.class))).thenAnswer(invocation -> {
            Candidate sourceCandidate = invocation.getArgument(0);
            CandidateDTO candidateDTO = new CandidateDTO();
            candidateDTO.setId(sourceCandidate.getId());
            candidateDTO.setFirstName(sourceCandidate.getFirstName());
            candidateDTO.setLastName(sourceCandidate.getLastName());
            candidateDTO.setPhoneNumber(sourceCandidate.getPhoneNumber());
            candidateDTO.setSex(sourceCandidate.getSex());
            candidateDTO.setBirthday(sourceCandidate.getBirthday());
            candidateDTO.setLinkAvt(sourceCandidate.getLinkAvt());

            return candidateDTO;
        });
    }



    @Test
    public void testGetCandidate() {
        // Arrange
        String authorizationHeader = "Bearer MOCK_JWT";
        Account account = new Account();
        account.setEmail("mock@example.com");
        Candidate candidate = new Candidate();
        candidate.setId(1);
        candidate.setFirstName("John");
        candidate.setLastName("Doe");
        candidate.setAccount(account);

        when(accountService.getCandidateByAuthorizationHeader(authorizationHeader)).thenReturn(candidate);

        // Act
        CandidateDTO result = candidateService.getCandidate(authorizationHeader);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
    }

    @Test
    public void testGetAll() {
        // Arrange
        List<Candidate> candidates = new ArrayList<>();
        candidates.add(Candidate.builder().id(1).firstName("John").lastName("Doe").build());
        candidates.add(Candidate.builder().id(2).firstName("Jane").lastName("Smith").build());

        // PageImpl is used to mock the Page object returned by the repository
        Page<Candidate> candidatePage = new PageImpl<>(candidates);

        // Set up the behavior of the mock objects
        when(candidateRepository.findAll(any(Pageable.class))).thenReturn(candidatePage);
        
        // Act
        List<CandidateDTO> result = candidateService.search(null, null);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Doe", result.get(0).getLastName());
        assertEquals("Jane", result.get(1).getFirstName());
        assertEquals("Smith", result.get(1).getLastName());
    }

    @Test
    public void testGetCandidateById_Success() {
        // Arrange
        Integer candidateId = 1;
        Candidate candidate = createMockCandidate(candidateId);
        when(candidateRepository.findCandidateById(candidateId)).thenReturn(candidate);

        CandidateDTO expectedCandidateDTO = createMockCandidateDTO(candidateId);

        // Act
        CandidateDTO resultCandidateDTO = candidateService.getCandidateById(candidateId);

        // Assert
        assertNotNull(resultCandidateDTO);
        assertEquals(expectedCandidateDTO.getId(), resultCandidateDTO.getId());
        assertEquals(expectedCandidateDTO.getFirstName(), resultCandidateDTO.getFirstName());
        assertEquals(expectedCandidateDTO.getLastName(), resultCandidateDTO.getLastName());
        assertEquals(expectedCandidateDTO.getPhoneNumber(), resultCandidateDTO.getPhoneNumber());
    }

    @Test
    public void testGetCandidateById_CandidateNotFound() {
        // Arrange
        Integer candidateId = 1;
        when(candidateRepository.findCandidateById(candidateId)).thenReturn(null);

        // Act and Assert
        assertThrows(NotFoundException.class, () -> candidateService.getCandidateById(candidateId));
    }

    @Test
    public void testUpdateCandidate_Success() {
        // Arrange
        Integer candidateId = 1;
        CandidateDTO candidateDTO = createMockCandidateDTO(candidateId);

        Candidate candidateToUpdate = createMockCandidate(candidateId);
        when(candidateRepository.findCandidateById(candidateId)).thenReturn(candidateToUpdate);

        when(candidateMapper.mapToEntity(any(CandidateDTO.class))).thenReturn(candidateToUpdate);

        Candidate updatedCandidate = createMockCandidate(candidateId);
        when(candidateRepository.save(any(Candidate.class))).thenReturn(updatedCandidate);

        CandidateDTO expectedUpdatedCandidateDTO = createMockCandidateDTO(candidateId);
        when(candidateMapper.mapToDTO(any(Candidate.class))).thenReturn(expectedUpdatedCandidateDTO);

        // Act
        CandidateDTO resultCandidateDTO = candidateService.updateTest(candidateDTO, candidateId);

        // Assert
        assertNotNull(resultCandidateDTO);
        assertEquals(expectedUpdatedCandidateDTO.getId(), resultCandidateDTO.getId());
        assertEquals(expectedUpdatedCandidateDTO.getFirstName(), resultCandidateDTO.getFirstName());
        assertEquals(expectedUpdatedCandidateDTO.getLastName(), resultCandidateDTO.getLastName());
        assertEquals(expectedUpdatedCandidateDTO.getPhoneNumber(), resultCandidateDTO.getPhoneNumber());
    }

    @Test
    public void testUpdateCandidate_CandidateNotFound() {
        // Arrange
        Integer candidateId = 1;
        CandidateDTO candidateDTO = createMockCandidateDTO(candidateId);

        when(candidateRepository.findCandidateById(candidateId)).thenReturn(null);

        // Act and Assert
        assertThrows(NotFoundException.class, () -> candidateService.updateTest(candidateDTO, candidateId));
    }

    @Test
    public void testApplyVacancy_Success() {
        // Arrange
        Integer vacancyId = 1;
        Integer cvId = 1;
        String token = "Toi la candidate";
        String authorizationHeader = "Bearer " + token;

        Integer candidateId = 1;
        Candidate candidate = createMockCandidate(candidateId);

        when(blacklistService.isCandidateAlreadyInBlacklist(candidateId)).thenReturn(false);
        when(accountService.getCandidateByAuthorizationHeader(authorizationHeader)).thenReturn(candidate);
        when(candidateRepository.findById(candidateId)).thenReturn(Optional.of(candidate));

        CV cv = createMockCV(candidateId);
        when(cvRepository.findCVById(cvId)).thenReturn(cv);

        Vacancy vacancy = createMockVacancy(vacancyId);
        when(vacancyRepository.findById(vacancyId)).thenReturn(Optional.of(vacancy));

        CandidateVacancy candidateVacancy = createMockCandidateVacancy(candidateId, vacancyId);
        when(candidateVacancyRepository.save(any(CandidateVacancy.class))).thenReturn(candidateVacancy);

        CandidateVacancyDTO expectedCandidateVacancyDTO = createMockCandidateVacancyDTO(candidateId, vacancyId);
        when(candidateVacancyMapper.mapToDTO(any(CandidateVacancy.class))).thenReturn(expectedCandidateVacancyDTO);

        // Act
        CandidateVacancyDTO candidateVacancyDTO = new CandidateVacancyDTO();
        candidateVacancyDTO.setVacancy(expectedCandidateVacancyDTO.getVacancy());
        candidateVacancyDTO.setCvId(cvId);
        CandidateVacancyDTO resultCandidateVacancyDTO = candidateService.applyVacancy(candidateVacancyDTO, authorizationHeader);

        // Assert
        assertNotNull(resultCandidateVacancyDTO);
        assertEquals(expectedCandidateVacancyDTO.getCandidate(), resultCandidateVacancyDTO.getCandidate());
        assertEquals(expectedCandidateVacancyDTO.getVacancy(), resultCandidateVacancyDTO.getVacancy());
        assertEquals(expectedCandidateVacancyDTO.getStatus(), resultCandidateVacancyDTO.getStatus());
    }

    // Helper methods to create mock objects
    private Candidate createMockCandidate(Integer id) {
        return Candidate.builder()
                .id(id)
                .firstName("John")
                .lastName("Doe")
                .phoneNumber("1234567890")
                .sex(1)
                .birthday(new Date(System.currentTimeMillis()))
                .linkAvt("profile.jpg")
                .cvs(new HashSet<>())
                .build();
    }

    private CandidateDTO createMockCandidateDTO(Integer id) {
        return new CandidateDTO(
                id,
                true,
                "John",
                "Doe",
                "1234567890",
                1,
                new Date(System.currentTimeMillis()),
                "profile.jpg",
                null,
                "abc@gmail.com",
                null
        );
    }

    private CV createMockCV(Integer candidateId) {
        return CV.builder()
                .id(1)
                .creatdDate(new Date(System.currentTimeMillis()))
                .fileName("resume.pdf")
                .linkCV("resume-link")
                .candidate(createMockCandidate(candidateId))
                .build();
    }

    private Vacancy createMockVacancy(Integer id) {
        return Vacancy.builder()
                .id(id)
                .description("We are looking for a skilled Software Engineer.")
                .build();
    }

    private VacancyDTO createMockVacancyDTO(Integer id) {
        VacancyDTO vacancyDTO = new VacancyDTO();
        vacancyDTO.setId(id);
        vacancyDTO.setDescription("We are looking for a skilled Software Engineer.");
        return vacancyDTO;
    }

    private CandidateVacancy createMockCandidateVacancy(Integer candidateId, Integer vacancyId) {
        Candidate candidate = createMockCandidate(candidateId);
        Vacancy vacancy = createMockVacancy(vacancyId);

        return CandidateVacancy.builder()
                .id(new CandidateVacancyKey(candidateId, vacancyId))
                .candidate(candidate)
                .vacancy(vacancy)
                .status("Pending")
                .cvId(1)
                .applyDate(new Date(System.currentTimeMillis()))
                .build();
    }

    private CandidateVacancyDTO createMockCandidateVacancyDTO(Integer candidateId, Integer vacancyId) {
        CandidateVacancyDTO candidateVacancyDTO = new CandidateVacancyDTO();
        candidateVacancyDTO.setCandidate(createMockCandidateDTO(candidateId));
        candidateVacancyDTO.setVacancy(createMockVacancyDTO(vacancyId));
        candidateVacancyDTO.setApplyDate(new Date(System.currentTimeMillis()));
        candidateVacancyDTO.setStatus("PENDING");
        candidateVacancyDTO.setCvId(1);
        return candidateVacancyDTO;
    }
}
