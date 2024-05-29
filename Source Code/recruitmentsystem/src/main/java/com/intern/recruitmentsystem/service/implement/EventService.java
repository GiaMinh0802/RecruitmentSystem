package com.fpt.recruitmentsystem.service.implement;

import com.fpt.recruitmentsystem.model.CollectedCandidate;
import com.fpt.recruitmentsystem.model.Event;
import com.fpt.recruitmentsystem.service.IEventService;
import com.fpt.recruitmentsystem.dto.EventFeaturesDTO;
import com.fpt.recruitmentsystem.exception.BadRequestException;
import com.fpt.recruitmentsystem.exception.NotFoundException;
import com.fpt.recruitmentsystem.mapper.EventMapper;
import com.fpt.recruitmentsystem.repository.CollectedCandidateRepository;
import com.fpt.recruitmentsystem.repository.EventRepository;
import com.fpt.recruitmentsystem.util.Message;
import com.fpt.recruitmentsystem.util.ResponseMessage;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService implements IEventService {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final CollectedCandidateRepository collectedCandidateRepository;

    public List<EventFeaturesDTO> getListEvent(){
        List<Event> events = eventRepository.findAll();
        if (events.isEmpty()){
            throw new NotFoundException(Message.EVENT_NOT_FOUND);
        }
        return events.stream().
                map(eventMapper::mapEventFeaturesToDTO).
                toList();
    }
    public EventFeaturesDTO getEventDetails(int id){
        Event event = eventRepository.findEventById(id);
        if (event == null){
            throw new NotFoundException(Message.EVENT_NOT_FOUND);
        }
        return eventMapper.mapEventFeaturesToDTO(event);
    }
    public List<EventFeaturesDTO> getEventByCollectedCandidate(int ccId){
        CollectedCandidate collectedCandidate = collectedCandidateRepository.findCollectedCandidateById(ccId);
        if (collectedCandidate == null){
            throw new NotFoundException(Message.COLLECTED_CANDIDATE_NOT_FOUND);
        }
        List<Event> events = eventRepository.findEventByCollectedCandidate(ccId);
        if (events.isEmpty()){
            throw new NotFoundException(Message.EVENT_NOT_FOUND);
        }
        return events.stream()
                .map(eventMapper::mapEventFeaturesToDTO).
                toList();
    }
    public EventFeaturesDTO addEvent(EventFeaturesDTO eventFeaturesDTO){
        try{
            Event eventToEntity = eventMapper.mapEventFeaturesToEntity(eventFeaturesDTO);
            Event eventNew = eventRepository.save(eventToEntity);
            return eventMapper.mapEventFeaturesToDTO(eventNew);
        } catch (Exception e){
            throw new BadRequestException("Can't create event: " + e.getMessage());
        }
    }
    public ResponseMessage deleteEvent(int id){
        Event event = eventRepository.findEventById(id);
        if (event == null) {
            throw new NotFoundException(Message.EVENT_NOT_FOUND);
        }
        try {
            eventRepository.delete(event);
            return new ResponseMessage("Deleted event with Id: " + id);

        } catch (Exception e){
            throw new BadRequestException("Can't delete event: " + e.getMessage());
        }
    }
    public EventFeaturesDTO updateEvent(int id, EventFeaturesDTO eventDTO){
        Event eventUpdate = eventRepository.findEventById(id);
        if (eventUpdate == null){
            throw new NotFoundException(Message.EVENT_NOT_FOUND);
        }
        try {
            Event eventToEntity = eventMapper.mapEventFeaturesToEntity(eventDTO);
            eventUpdate.setName(eventToEntity.getName());
            eventUpdate.setStartDate(eventToEntity.getStartDate());
            eventUpdate.setEndDate(eventToEntity.getEndDate());
            eventUpdate.setLocation(eventToEntity.getLocation());
            eventUpdate.setDescription(eventToEntity.getDescription());
            eventUpdate.setStatus(eventToEntity.getStatus());
            eventUpdate.setRule(eventToEntity.getRule());
            eventUpdate.setBenefit(eventToEntity.getBenefit());
            eventUpdate.setRecruiter(eventToEntity.getRecruiter());
            Event eventNew = eventRepository.save(eventUpdate);
            return eventMapper.mapEventFeaturesToDTO(eventNew);
        } catch (Exception e){
            throw new BadRequestException("Can't update event: " + e.getMessage());
        }
    }
    public List<EventFeaturesDTO> findPaginated(int pageNo, int pageLimit) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageLimit, Sort.by(Sort.Direction.DESC, "startDate"));
        Page<Event> eventList = eventRepository.findAll(pageable);
        if (eventList.isEmpty()) {
            throw new NotFoundException(Message.EVENT_NOT_FOUND);
        }
        return eventList.stream()
                .map(eventMapper::mapEventFeaturesToDTO)
                .toList();
    }
    
}
