package com.fpt.recruitmentsystem.service;

import com.fpt.recruitmentsystem.dto.CollectedCandidateDTO;
import com.fpt.recruitmentsystem.dto.CollectedCandidateFeaturesDTO;
import com.fpt.recruitmentsystem.util.ResponseMessage;

import java.util.List;

public interface ICollectedCandidateService {
    List<CollectedCandidateFeaturesDTO> getListCollectedCandidate();
    List<CollectedCandidateFeaturesDTO> getListCollectedCandidateByEvent(int eventId);
    ResponseMessage deleteCollectedCandidate(int id);
    CollectedCandidateFeaturesDTO getCollectedCandidateDetails(int id);
    CollectedCandidateDTO addCollectedCandidate(int eventId, CollectedCandidateDTO collectedCandidateDTO);
    List<CollectedCandidateFeaturesDTO> findPaginated(int pageNo, int pageLimit);
}
