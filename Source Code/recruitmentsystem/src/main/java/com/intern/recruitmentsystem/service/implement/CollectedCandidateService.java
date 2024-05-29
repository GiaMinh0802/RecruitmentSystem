package com.fpt.recruitmentsystem.service.implement;

import com.fpt.recruitmentsystem.model.CollectedCandidate;
import com.fpt.recruitmentsystem.model.Event;
import com.fpt.recruitmentsystem.service.ICollectedCandidateService;
import com.fpt.recruitmentsystem.dto.CollectedCandidateDTO;
import com.fpt.recruitmentsystem.dto.CollectedCandidateFeaturesDTO;
import com.fpt.recruitmentsystem.exception.BadRequestException;
import com.fpt.recruitmentsystem.exception.NotFoundException;
import com.fpt.recruitmentsystem.mapper.CollectedCandidateMapper;
import com.fpt.recruitmentsystem.repository.CollectedCandidateRepository;
import com.fpt.recruitmentsystem.repository.EventRepository;
import com.fpt.recruitmentsystem.service.IMailService;
import com.fpt.recruitmentsystem.util.Message;
import com.fpt.recruitmentsystem.util.ResponseMessage;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CollectedCandidateService implements ICollectedCandidateService {
    private final CollectedCandidateRepository collectedCandidateRepository;
    private final CollectedCandidateMapper collectedCandidateMapper;
    private final EventRepository eventRepository;
    private final IMailService mailService;

    public List<CollectedCandidateFeaturesDTO> getListCollectedCandidate(){
        List<CollectedCandidate> collectedCandidates = collectedCandidateRepository.findAll();
        if (collectedCandidates.isEmpty()){
            throw new NotFoundException(Message.COLLECTED_CANDIDATE_NOT_FOUND);
        }
        return collectedCandidates.stream().
                map(collectedCandidateMapper::mapCCToDTO).
                toList();
    }
    public CollectedCandidateFeaturesDTO getCollectedCandidateDetails(int id){
        CollectedCandidate collectedCandidate = collectedCandidateRepository.findCollectedCandidateById(id);
        if (collectedCandidate == null){
            throw new NotFoundException(Message.COLLECTED_CANDIDATE_NOT_FOUND);
        }
        return collectedCandidateMapper.mapCCToDTO(collectedCandidate);
    }

    public CollectedCandidateDTO addCollectedCandidate(int eventId, CollectedCandidateDTO collectedCandidateDTO){
        CollectedCandidate collectedCandidate = assureCollectedCandidateExist(collectedCandidateDTO.getEmail());
        Event event = eventRepository.findEventById(eventId);
        if (event == null){
            throw new NotFoundException(Message.EVENT_NOT_FOUND);
        }
        if (collectedCandidate==null){
            return collectedCandidateNonExist(event, collectedCandidateDTO);
        } else {
            return collectedCandidateExist(collectedCandidate.getId(), event, collectedCandidateDTO);
        }
    }
    public CollectedCandidate assureCollectedCandidateExist (String email){
        return collectedCandidateRepository.findCollectedCandidateByEmail(email);
    }

    public CollectedCandidateDTO collectedCandidateExist(int id, Event event, CollectedCandidateDTO collectedCandidateDTO){
        CollectedCandidate existingCC = collectedCandidateRepository.findCollectedCandidateById(id);
        BeanUtils.copyProperties(collectedCandidateDTO, existingCC, "id");
        existingCC.getEvents().add(event);
        collectedCandidateRepository.save(existingCC);
        event.getEventCollectedCandidate().add(existingCC);
        eventRepository.save(event);
        mailService.sendApplyEventSuccess(event, collectedCandidateDTO);
        return collectedCandidateMapper.mapToDTO(existingCC);
    }
    public CollectedCandidateDTO collectedCandidateNonExist(Event event, CollectedCandidateDTO collectedCandidateDTO){
        CollectedCandidate collectedCandidateToEntity = collectedCandidateMapper.mapToEntity(collectedCandidateDTO);
        collectedCandidateToEntity.getEvents().add(event);
        CollectedCandidate collectedCandidateNew = collectedCandidateRepository.save(collectedCandidateToEntity);
        event.getEventCollectedCandidate().add(collectedCandidateToEntity);
        eventRepository.save(event);
        mailService.sendApplyEventSuccess(event, collectedCandidateDTO);
        return collectedCandidateMapper.mapToDTO(collectedCandidateNew);
    }
    public List<CollectedCandidateFeaturesDTO> getListCollectedCandidateByEvent(int eventId){
        Event event = eventRepository.findEventById(eventId);
        if (event == null){
            throw new NotFoundException(Message.EVENT_NOT_FOUND);
        }
        List<CollectedCandidate> collectedCandidateList = collectedCandidateRepository.findCollectedCandidateByEvent(eventId);
        if (collectedCandidateList.isEmpty()){
            throw new NotFoundException(Message.COLLECTED_CANDIDATE_NOT_FOUND);
        }
        return collectedCandidateList.stream().
                map(collectedCandidateMapper::mapCCToDTO).
                toList();
    }
    public ResponseMessage deleteCollectedCandidate(int id){
        CollectedCandidate collectedCandidate = collectedCandidateRepository.findCollectedCandidateById(id);
        if (collectedCandidate == null){
            throw new NotFoundException(Message.COLLECTED_CANDIDATE_NOT_FOUND);
        }
        try {
            collectedCandidateRepository.delete(collectedCandidate);
            return new ResponseMessage("Deleted collected candidate with Id: " + id);
        } catch (Exception e){
            throw new BadRequestException("Can't delete collected candidate: " + e.getMessage());
        }
    }
    public List<CollectedCandidateFeaturesDTO> findPaginated(int pageNo, int pageLimit){
        Pageable pageable = PageRequest.of(pageNo-1, pageLimit);
        Page<CollectedCandidate> page = collectedCandidateRepository.findAll(pageable);
        List<CollectedCandidate> collectedCandidateList = page.getContent();
        if (collectedCandidateList.isEmpty()){
            throw new NotFoundException(Message.COLLECTED_CANDIDATE_NOT_FOUND);
        }
        return collectedCandidateList.stream().
                map(collectedCandidateMapper::mapCCToDTO).
                toList();
    }

}
