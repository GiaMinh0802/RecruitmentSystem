package com.fpt.recruitmentsystem.mapper;

import com.fpt.recruitmentsystem.model.CollectedCandidate;
import com.fpt.recruitmentsystem.model.Event;
import com.fpt.recruitmentsystem.dto.CollectedCandidateDTO;
import com.fpt.recruitmentsystem.dto.CollectedCandidateFeaturesDTO;
import com.fpt.recruitmentsystem.dto.EventFeaturesDTO;
import com.fpt.recruitmentsystem.repository.EventRepository;

import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class CollectedCandidateMapper {
    private final ModelMapper modelMapper;
    private final EventRepository eventRepository;

    public CollectedCandidate mapToEntity(CollectedCandidateDTO collectedCandidateDTO) {
        CollectedCandidate collectedCandidate = modelMapper.map(collectedCandidateDTO, CollectedCandidate.class);
        collectedCandidate.setEvents(getListEventEntity(collectedCandidateDTO));
        return collectedCandidate;
    }

    public CollectedCandidateDTO mapToDTO(CollectedCandidate collectedCandidate) {
        return modelMapper.map(collectedCandidate, CollectedCandidateDTO.class);
    }
    public CollectedCandidateFeaturesDTO mapCCToDTO(CollectedCandidate collectedCandidate){
        return modelMapper.map(collectedCandidate, CollectedCandidateFeaturesDTO.class);
    }
    public Set<Event> getListEventEntity(CollectedCandidateDTO collectedCandidateDTO) {
        List<Event> eventList = new ArrayList<>();
        for (EventFeaturesDTO eventDTO : collectedCandidateDTO.getEvents()) {
            Event event = eventRepository.findEventById(eventDTO.getId());
            event.setId(event.getId());
            event.setName(event.getName());
            event.setCreateAt(event.getCreateAt());
            event.setStartDate(event.getStartDate());
            event.setEndDate(event.getEndDate());
            event.setLocation(event.getLocation());
            event.setDescription(event.getDescription());
            event.setStatus(event.getStatus());
            event.setRecruiter(event.getRecruiter());
            eventList.add(event);
        }
        return new HashSet<>(eventList);
    }
}
