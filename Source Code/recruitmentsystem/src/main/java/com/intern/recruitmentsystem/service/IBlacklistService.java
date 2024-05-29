package com.fpt.recruitmentsystem.service;

import com.fpt.recruitmentsystem.dto.BlacklistDTO;
import com.fpt.recruitmentsystem.dto.CandidateDTO;

import java.util.List;

public interface IBlacklistService {
    boolean isCandidateAlreadyInBlacklist(int candidateId);
    List<BlacklistDTO> getBlacklistHistory(int candidateId);
    List<CandidateDTO> getAllCandidateInBlacklist();
    BlacklistDTO insert(int candidateId, BlacklistDTO newRecordDto);
    BlacklistDTO unblacklist(int candidateId);
    List<BlacklistDTO> findPaginated(Integer pageNo, Integer pageSize);
}
