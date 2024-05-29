package com.fpt.recruitmentsystem.controller.recruiter;

import com.fpt.recruitmentsystem.service.IEventService;
import com.fpt.recruitmentsystem.dto.EventFeaturesDTO;
import com.fpt.recruitmentsystem.util.ResponseMessage;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recruiters/events")
@SecurityRequirement(name="bearerAuth")
@Tag(name = "Recruiter",description = "Recruiter Controller")
@RequiredArgsConstructor
public class EventController {
    private final IEventService eventService;

    @GetMapping
    public ResponseEntity<List<EventFeaturesDTO>> getListEvent(){
        return new ResponseEntity<>(eventService.getListEvent(), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<EventFeaturesDTO> getEventDetails(@PathVariable int id){
        return new ResponseEntity<>(eventService.getEventDetails(id), HttpStatus.OK);
    }
    @GetMapping("/paging")
    public ResponseEntity<List<EventFeaturesDTO>> findPaginated(@RequestParam int page, @RequestParam int limit){
        return new ResponseEntity<>(eventService.findPaginated(page, limit), HttpStatus.OK);
    }
    @GetMapping("/collected-candidates")
    public ResponseEntity<List<EventFeaturesDTO>> getEventByCollectedCandidateId(@RequestParam int ccId){
        return new ResponseEntity<>(eventService.getEventByCollectedCandidate(ccId), HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<EventFeaturesDTO> addEvent(@RequestBody EventFeaturesDTO eventFeaturesDTO){
        return new ResponseEntity<>(eventService.addEvent(eventFeaturesDTO), HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<EventFeaturesDTO> updateEvent(@PathVariable int id, @RequestBody EventFeaturesDTO eventFeaturesDTO){
        return new ResponseEntity<>(eventService.updateEvent(id, eventFeaturesDTO), HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseMessage> deleteEvent(@PathVariable int id){
        return new ResponseEntity<>(eventService.deleteEvent(id), HttpStatus.OK);
    }
}
