package com.fpt.recruitmentsystem.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fpt.recruitmentsystem.model.Event;
import com.fpt.recruitmentsystem.dto.EventFeaturesDTO;
import com.fpt.recruitmentsystem.exception.BadRequestException;
import com.fpt.recruitmentsystem.exception.NotFoundException;
import com.fpt.recruitmentsystem.mapper.EventMapper;
import com.fpt.recruitmentsystem.repository.EventRepository;
import com.fpt.recruitmentsystem.service.implement.EventService;
import com.fpt.recruitmentsystem.util.ResponseMessage;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private EventMapper eventMapper;


    @InjectMocks
    private EventService eventService;

    @Test
    void testGetListEvent_Success() {
        // Arrange
        List<Event> events = new ArrayList<>();
        events.add(Event.builder()
                .id(1)
                .name("Event 1")
                .startDate(new Date(System.currentTimeMillis()))
                .endDate(new Date(System.currentTimeMillis()))
                .location("Location 1")
                .description("Description 1")
                .status("Status 1")
                .rule("Rule 1")
                .benefit("Benefit 1")
                .recruiter(null)
                .eventCollectedCandidate(null)
                .build());
        when(eventRepository.findAll()).thenReturn(events);

        List<EventFeaturesDTO> eventFeaturesDTOs = new ArrayList<>();
        EventFeaturesDTO eventFeaturesDTO = new EventFeaturesDTO();
        eventFeaturesDTO.setId(1);
        eventFeaturesDTO.setName("Event 1");
        eventFeaturesDTO.setStartDate(new Date(System.currentTimeMillis()));
        eventFeaturesDTO.setEndDate(new Date(System.currentTimeMillis()));
        eventFeaturesDTO.setLocation("Location 1");
        eventFeaturesDTO.setDescription("Description 1");
        eventFeaturesDTO.setStatus("Status 1");
        eventFeaturesDTO.setRule("Rule 1");
        eventFeaturesDTO.setBenefit("Benefit 1");
        eventFeaturesDTO.setRecruiter(null);
        // eventFeaturesDTO.setEventCollectedCandidate(null);
        eventFeaturesDTOs.add(eventFeaturesDTO);
        when(eventMapper.mapEventFeaturesToDTO(any(Event.class))).thenReturn(eventFeaturesDTO);

        // Act
        List<EventFeaturesDTO> result = eventService.getListEvent();

        // Assert
        assertEquals(1, result.size());
        assertEquals("Event 1", result.get(0).getName());
        assertEquals("Location 1", result.get(0).getLocation());
    }

    @Test
    void testGetListEvent_NoEventsFound() {
        assertThrows(NotFoundException.class, () -> eventService.getListEvent());
    }

    @Test
    void testGetEventDetails_Success() {
        // Arrange
        int eventId = 1;
        Event event = Event.builder()
                .id(eventId)
                .name("Event 1")
                .startDate(new Date(System.currentTimeMillis()))
                .endDate(new Date(System.currentTimeMillis()))
                .location("Location 1")
                .description("Description 1")
                .status("Status 1")
                .rule("Rule 1")
                .benefit("Benefit 1")
                .recruiter(null)
                .eventCollectedCandidate(null)
                .build();
        when(eventRepository.findEventById(eventId)).thenReturn(event);

        EventFeaturesDTO eventDTO = new EventFeaturesDTO();
        eventDTO.setId(eventId);
        eventDTO.setName("Event 1");
        eventDTO.setStartDate(new Date(System.currentTimeMillis()));
        eventDTO.setEndDate(new Date(System.currentTimeMillis()));
        eventDTO.setLocation("Location 1");
        eventDTO.setDescription("Description 1");
        eventDTO.setStatus("Status 1");
        eventDTO.setRule("Rule 1");
        eventDTO.setBenefit("Benefit 1");
        eventDTO.setRecruiter(null);
        when(eventMapper.mapEventFeaturesToDTO(event)).thenReturn(eventDTO);

        // Act
        EventFeaturesDTO result = eventService.getEventDetails(eventId);

        // Assert
        assertNotNull(result);
        assertEquals(eventId, result.getId());
        assertEquals("Event 1", result.getName());
    }

    @Test
    void testGetEventDetails_EventNotFound() {
        // Arrange
        int eventId = 1;
        // Act & Assert
        assertThrows(NotFoundException.class, () -> eventService.getEventDetails(eventId));
    }

    @Test
    void testAddEvent_Success() {
        // Arrange
        EventFeaturesDTO eventFeaturesDTO = new EventFeaturesDTO();
        eventFeaturesDTO.setName("New Event");
        eventFeaturesDTO.setStartDate(new Date(System.currentTimeMillis()));
        eventFeaturesDTO.setEndDate(new Date(System.currentTimeMillis()));
        eventFeaturesDTO.setLocation("Location 1");
        eventFeaturesDTO.setDescription("Description 1");
        eventFeaturesDTO.setStatus("Status 1");
        eventFeaturesDTO.setRule("Rule 1");
        eventFeaturesDTO.setBenefit("Benefit 1");
        eventFeaturesDTO.setRecruiter(null);

        Event eventToEntity = Event.builder()
                .name("New Event")
                .startDate(new Date(System.currentTimeMillis()))
                .endDate(new Date(System.currentTimeMillis()))
                .location("Location 1")
                .description("Description 1")
                .status("Status 1")
                .rule("Rule 1")
                .benefit("Benefit 1")
                .recruiter(null)
                .build();

        Event eventNew = Event.builder()
                .id(1)
                .name("New Event")
                .startDate(new Date(System.currentTimeMillis()))
                .endDate(new Date(System.currentTimeMillis()))
                .location("Location 1")
                .description("Description 1")
                .status("Status 1")
                .rule("Rule 1")
                .benefit("Benefit 1")
                .recruiter(null)
                .build();

        when(eventMapper.mapEventFeaturesToEntity(eventFeaturesDTO)).thenReturn(eventToEntity);
        when(eventRepository.save(eventToEntity)).thenReturn(eventNew);
        when(eventMapper.mapEventFeaturesToDTO(eventNew)).thenReturn(eventFeaturesDTO);

        // Act
        EventFeaturesDTO result = eventService.addEvent(eventFeaturesDTO);

        // Assert
        assertNotNull(result);
        assertEquals("New Event", result.getName());
    }

    @Test
    void testAddEvent_Failure() {
        // Arrange
        EventFeaturesDTO eventFeaturesDTO = new EventFeaturesDTO();
        eventFeaturesDTO.setName("New Event");
        // Set other properties as required.

        when(eventMapper.mapEventFeaturesToEntity(eventFeaturesDTO)).thenThrow(new RuntimeException("Mock Exception"));

        // Act & Assert
        assertThrows(BadRequestException.class, () -> eventService.addEvent(eventFeaturesDTO));
    }

    @Test
    void testUpdateEvent_Success() {
        // Arrange
        int eventId = 1;
        EventFeaturesDTO eventDTO = new EventFeaturesDTO();
        eventDTO.setId(eventId);
        eventDTO.setName("Updated Event");

        Event existingEvent = Event.builder()
                .id(eventId)
                .name("Old Event")
                .startDate(new Date(System.currentTimeMillis()))
                .endDate(new Date(System.currentTimeMillis()))
                .location("Location 1")
                .description("Description 1")
                .status("Status 1")
                .rule("Rule 1")
                .benefit("Benefit 1")
                .recruiter(null)
                .eventCollectedCandidate(null)
                .build();

        Event updatedEvent = Event.builder()
                .id(eventId)
                .name("Updated Event")
                .startDate(new Date(System.currentTimeMillis()))
                .endDate(new Date(System.currentTimeMillis()))
                .location("Location 1")
                .description("Description 1")
                .status("Status 1")
                .rule("Rule 1")
                .benefit("Benefit 1")
                .recruiter(null)
                .eventCollectedCandidate(null)
                .build();

        when(eventRepository.findEventById(eventId)).thenReturn(existingEvent);
        when(eventRepository.save(existingEvent)).thenReturn(updatedEvent);
        when(eventMapper.mapEventFeaturesToEntity(eventDTO)).thenReturn(updatedEvent);
        when(eventMapper.mapEventFeaturesToDTO(updatedEvent)).thenReturn(eventDTO);

        // Act
        EventFeaturesDTO result = eventService.updateEvent(eventId, eventDTO);

        // Assert
        assertNotNull(result);
        assertEquals(eventId, result.getId());
        assertEquals("Updated Event", result.getName());
    }

    @Test
    void testUpdateEvent_EventNotFound() {
        // Arrange
        int eventId = 1;
        when(eventRepository.findEventById(eventId)).thenReturn(null);

        // Act & Assert
        assertThrows(NotFoundException.class, () -> eventService.updateEvent(eventId, new EventFeaturesDTO()));
    }

    @Test
    void testUpdateEvent_Failure() {
        // Arrange
        int eventId = 1;
        EventFeaturesDTO eventDTO = new EventFeaturesDTO();
        eventDTO.setName("Updated Event");
        // Set other properties as required.

        Event existingEvent = Event.builder()
                .id(eventId)
                .name("Old Event")
                .startDate(new Date(System.currentTimeMillis()))
                .endDate(new Date(System.currentTimeMillis()))
                .location("Location 1")
                .description("Description 1")
                .status("Status 1")
                .rule("Rule 1")
                .benefit("Benefit 1")
                .recruiter(null)
                .eventCollectedCandidate(null)
                .build();

        when(eventRepository.findEventById(eventId)).thenReturn(existingEvent);
        when(eventRepository.save(existingEvent)).thenThrow(new RuntimeException("Mock Exception"));
        when(eventMapper.mapEventFeaturesToEntity(eventDTO)).thenReturn(existingEvent);

        // Act & Assert
        assertThrows(BadRequestException.class, () -> eventService.updateEvent(eventId, eventDTO));
    }

    @Test
    void testDeleteEvent_Success() {
        // Arrange
        int eventId = 1;
        Event eventToDelete = Event.builder()
                .id(eventId)
                .name("Event 1")
                .startDate(new Date(System.currentTimeMillis()))
                .endDate(new Date(System.currentTimeMillis()))
                .location("Location 1")
                .description("Description 1")
                .status("Status 1")
                .rule("Rule 1")
                .benefit("Benefit 1")
                .recruiter(null)
                .eventCollectedCandidate(null)
                .build();

        when(eventRepository.findEventById(eventId)).thenReturn(eventToDelete);
        doNothing().when(eventRepository).delete(eventToDelete);

        // Act
        ResponseMessage result = eventService.deleteEvent(eventId);

        // Assert
        assertNotNull(result);
        assertEquals("Deleted event with Id: " + eventId, result.getMessage());
    }

    @Test
    void testDeleteEvent_EventNotFound() {
        // Arrange
        int eventId = 1;
        when(eventRepository.findEventById(eventId)).thenReturn(null);

        // Act & Assert
        assertThrows(NotFoundException.class, () -> eventService.deleteEvent(eventId));
    }

    @Test
    void testDeleteEvent_Failure() {
        // Arrange
        int eventId = 1;
        Event eventToDelete = Event.builder()
                .id(eventId)
                .name("Event 1")
                .startDate(new Date(System.currentTimeMillis()))
                .endDate(new Date(System.currentTimeMillis()))
                .location("Location 1")
                .description("Description 1")
                .status("Status 1")
                .rule("Rule 1")
                .benefit("Benefit 1")
                .recruiter(null)
                .eventCollectedCandidate(null)
                .build();

        when(eventRepository.findEventById(eventId)).thenReturn(eventToDelete);
        doThrow(new RuntimeException("Mock Exception")).when(eventRepository).delete(eventToDelete);

        // Act & Assert
        assertThrows(BadRequestException.class, () -> eventService.deleteEvent(eventId));
    }
}
