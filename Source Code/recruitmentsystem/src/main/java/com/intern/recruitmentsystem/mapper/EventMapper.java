package com.fpt.recruitmentsystem.mapper;

import com.fpt.recruitmentsystem.model.CollectedCandidate;
import com.fpt.recruitmentsystem.model.Event;

import com.fpt.recruitmentsystem.model.Recruiter;
import com.fpt.recruitmentsystem.dto.CollectedCandidateFeaturesDTO;
import com.fpt.recruitmentsystem.dto.EventDTO;
import com.fpt.recruitmentsystem.dto.EventFeaturesDTO;
import com.fpt.recruitmentsystem.dto.RecruiterDTO;
import com.fpt.recruitmentsystem.repository.CollectedCandidateRepository;
import com.fpt.recruitmentsystem.repository.RecruiterRepository;

import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class EventMapper {
    private final ModelMapper modelMapper;
    private final RecruiterMapper recruiterMapper;
    private final RecruiterRepository recruiterRepository;
    private final CollectedCandidateRepository collectedCandidateRepository;

    public Event mapToEntity(EventDTO eventDTO){
        Event event = modelMapper.map(eventDTO, Event.class);
        Recruiter recruiter = recruiterRepository.findRecruiterById(eventDTO.getRecruiter().getId());
        event.setRecruiter(recruiter);
        event.setEventCollectedCandidate(getListCollectedCandidateEntity(eventDTO));
        return event;
    }
    public Event mapEventFeaturesToEntity(EventFeaturesDTO eventFeaturesDTO){
        Event event = modelMapper.map(eventFeaturesDTO, Event.class);
        Recruiter recruiter = recruiterRepository.findRecruiterById(eventFeaturesDTO.getRecruiter().getId());
        event.setRecruiter(recruiter);
        return event;
    }
    public EventFeaturesDTO mapEventFeaturesToDTO(Event event){
        EventFeaturesDTO eventFeaturesDTO = modelMapper.map(event, EventFeaturesDTO.class);
        RecruiterDTO recruiterDTO = recruiterMapper.mapToDTO(event.getRecruiter());
        eventFeaturesDTO.setRecruiter(recruiterDTO);
        return eventFeaturesDTO;
    }
    public EventDTO mapToDTO(Event event){
        EventDTO eventDTO = modelMapper.map(event, EventDTO.class);
        RecruiterDTO recruiterDTO = recruiterMapper.mapToDTO(event.getRecruiter());
        eventDTO.setRecruiter(recruiterDTO);
        return eventDTO;
    }
    public Set<CollectedCandidate> getListCollectedCandidateEntity(EventDTO eventDTO){
        List<CollectedCandidate> collectedCandidateList = new ArrayList<>();
        for (CollectedCandidateFeaturesDTO collectedCandidateDTO: eventDTO.getEventCollectedCandidate()){
            CollectedCandidate collectedCandidate = collectedCandidateRepository.findCollectedCandidateById(collectedCandidateDTO.getId());
            collectedCandidate.setId(collectedCandidate.getId());
            collectedCandidate.setFirstName(collectedCandidate.getFirstName());
            collectedCandidate.setLastName(collectedCandidate.getLastName());
            collectedCandidate.setPhoneNumber(collectedCandidate.getPhoneNumber());
            collectedCandidate.setEmail(collectedCandidate.getEmail());
            collectedCandidate.setSex(collectedCandidate.getSex());
            collectedCandidate.setBirthday(collectedCandidate.getBirthday());
            collectedCandidateList.add(collectedCandidate);
        }
        return new HashSet<>(collectedCandidateList);
    }
}
