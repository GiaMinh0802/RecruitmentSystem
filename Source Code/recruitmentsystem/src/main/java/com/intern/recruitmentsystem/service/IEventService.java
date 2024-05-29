package com.fpt.recruitmentsystem.service;

import com.fpt.recruitmentsystem.dto.EventFeaturesDTO;
import com.fpt.recruitmentsystem.util.ResponseMessage;

import java.util.List;

public interface IEventService {
    List<EventFeaturesDTO> getListEvent();
    EventFeaturesDTO addEvent(EventFeaturesDTO eventFeaturesDTO);
    EventFeaturesDTO updateEvent(int id, EventFeaturesDTO eventFeaturesDTO);
    ResponseMessage deleteEvent(int id);
    List<EventFeaturesDTO> findPaginated (int pageNo, int pageLimit);
    EventFeaturesDTO getEventDetails(int id);
    List<EventFeaturesDTO> getEventByCollectedCandidate(int ccId);
}
