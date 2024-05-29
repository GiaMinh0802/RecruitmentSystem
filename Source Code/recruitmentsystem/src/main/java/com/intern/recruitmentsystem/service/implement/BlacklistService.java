package com.fpt.recruitmentsystem.service.implement;

import java.sql.Date;
import java.util.List;

import com.fpt.recruitmentsystem.dto.BlacklistDTO;
import com.fpt.recruitmentsystem.dto.CandidateDTO;
import com.fpt.recruitmentsystem.exception.BadRequestException;
import com.fpt.recruitmentsystem.exception.NotFoundException;
import com.fpt.recruitmentsystem.mapper.BlacklistMapper;
import com.fpt.recruitmentsystem.mapper.CandidateMapper;
import com.fpt.recruitmentsystem.repository.BlacklistRepository;
import com.fpt.recruitmentsystem.repository.CandidateRepository;
import com.fpt.recruitmentsystem.util.Message;
import com.fpt.recruitmentsystem.util.Utility;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fpt.recruitmentsystem.model.Blacklist;
import com.fpt.recruitmentsystem.model.Candidate;
import com.fpt.recruitmentsystem.service.IBlacklistService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BlacklistService implements IBlacklistService {
    private final BlacklistRepository blacklistRepository;
    private final CandidateRepository candidateRepository;
    private final BlacklistMapper blacklistMapper;
    private final CandidateMapper candidateMapper;

    private Candidate assureCandidateExist(int candidateId) {
        return candidateRepository.findById(candidateId)
                .orElseThrow(() -> new NotFoundException(Message.CANDIDATE_NOT_FOUND));
    }

    public boolean isCandidateAlreadyInBlacklist(int candidateId) {
        Blacklist latestRecord = blacklistRepository.findFirstByCandidateIdOrderByStartDateDesc(candidateId);
        Date currentDate = new Date(System.currentTimeMillis());
        
        if (latestRecord != null) {
            return latestRecord.getEndDate() == null || latestRecord.getEndDate().after(currentDate);
        }
        
        return false;
    }

    public List<BlacklistDTO> getBlacklistHistory(int candidateId) {
        assureCandidateExist(candidateId);

        List<Blacklist> blacklistHistory = blacklistRepository.findAllByCandidateId(candidateId);
        if (blacklistHistory.isEmpty()) {
            throw new NotFoundException(Message.BLACKLIST_NOT_FOUND);
        }
        return blacklistHistory.stream()
                .map(blacklistMapper::mapToDTO)
                .toList();
    }

    public BlacklistDTO insert(int candidateId, BlacklistDTO newRecordDto) {
        Blacklist newRecord = blacklistMapper.mapToEntity(newRecordDto);
        Candidate candidate = assureCandidateExist(candidateId);
        
        if (isCandidateAlreadyInBlacklist(candidateId)) {
            throw new BadRequestException("Candidate is already in black list");
        }

        newRecord.setCandidate(candidate);
        // the start date is determined by the time the record is added, not by user input
        Date currentDate = new Date(System.currentTimeMillis());
        newRecord.setStartDate(currentDate);   

        Blacklist newBlacklist = blacklistRepository.save(newRecord);
        return blacklistMapper.mapToDTO(newBlacklist);
    }

    public BlacklistDTO unblacklist(int candidateId) {
        assureCandidateExist(candidateId);
        
        if (!isCandidateAlreadyInBlacklist(candidateId)) {
            throw new BadRequestException("Candidate is not in blacklist");
        }

        Blacklist latestRecord = blacklistRepository.findFirstByCandidateIdOrderByStartDateDesc(candidateId);
        Date currentDate = new Date(System.currentTimeMillis());
        latestRecord.setEndDate(currentDate);
        Blacklist updatedRecord = blacklistRepository.save(latestRecord);
        return blacklistMapper.mapToDTO(updatedRecord);
    }
    public List<BlacklistDTO> findPaginated (Integer pageNo, Integer pageSize){
        Pageable pageable = Utility.getPageable(pageNo, pageSize);
        Page<Blacklist> blacklistList = blacklistRepository.findAll(pageable);
        if (blacklistList.isEmpty()) {
            throw new NotFoundException(Message.BLACKLIST_NOT_FOUND);
        }
        return blacklistList.stream().
                map(blacklistMapper::mapToDTO).
                toList();
    }

    public List<CandidateDTO> getAllCandidateInBlacklist() {
    	Date currentDate = new Date(System.currentTimeMillis());
    	List<Candidate> candidates = blacklistRepository.findCandidateByEndDateIsNullOrEndDateAfter(currentDate);

    	if (candidates.isEmpty()) {
			throw new NotFoundException(Message.CANDIDATE_NOT_FOUND);
		}

    	return candidates.stream()
                .map(candidateMapper::mapToDTO)
	            .toList();
	}
}
