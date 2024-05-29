package com.fpt.recruitmentsystem.controller.recruiter;

import com.fpt.recruitmentsystem.model.Interview;
import com.fpt.recruitmentsystem.service.ICvService;
import com.fpt.recruitmentsystem.service.IInterviewService;
import com.fpt.recruitmentsystem.service.implement.AuthCalendarService;
import com.fpt.recruitmentsystem.dto.CvDTO;
import com.fpt.recruitmentsystem.dto.InterviewDTO;


import com.google.api.services.calendar.model.Event;
import jakarta.servlet.http.HttpServletRequest;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("RecruiterInterviewController")
@RequestMapping("/recruiters/interviews")
@RequiredArgsConstructor
@SecurityRequirement(name="bearerAuth")
@Tag(name = "Recruiter",description = "Recruiter Controller")
public class InterviewController {
    private final IInterviewService interviewService;
    private final ICvService cvService;
    private final AuthCalendarService authCalendarService;

    @GetMapping("/search")
    public ResponseEntity<List<InterviewDTO>> search(
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer limit,
        @RequestParam(required = false) Integer vacancyId
    ) {
        List<InterviewDTO> interviews = interviewService.getAllInterview(page, limit, vacancyId);
        return new ResponseEntity<>(interviews, HttpStatus.OK);
    }
    @GetMapping("/details/{interviewId}")
    public ResponseEntity<InterviewDTO> getInterviewDetail(@PathVariable int interviewId){
        return new ResponseEntity<>(interviewService.getInterviewById(interviewId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<InterviewDTO> addInterview(@RequestBody InterviewDTO interviewDTO,
                                                     @Parameter(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization)  {
        InterviewDTO createdInterview = interviewService.addInterview(interviewDTO,authorization);
        return new ResponseEntity<>(createdInterview, HttpStatus.CREATED);
    }


    @GetMapping(value = "/auth/calendar")
    public ResponseEntity<String> googleConnectionStatus(HttpServletRequest request) throws Exception {
        return new ResponseEntity<>(authCalendarService.authorize(), HttpStatus.OK);
    }

    @GetMapping(value = "/create/calendar")
    public ResponseEntity<String> createEvent(@RequestParam(value = "code") String code,
                                              @RequestParam(value = "id") int id){
        Interview interview = interviewService.getInterviewByIdUnique(id);
        Event event = interviewService.createEvent(code, interview);
        return new ResponseEntity<>(event.getHangoutLink(), HttpStatus.CREATED);
    }

    @PutMapping(value = "updateLinkMeeting")
    public ResponseEntity<InterviewDTO> updateLinkMeeting(@RequestParam(value = "id") int id,
                                                          @RequestParam(value = "link") String link){
        return new ResponseEntity<>(interviewService.updateLinkGoogleMeet(id, link), HttpStatus.OK);
    }

    @PostMapping("interviewers")
    public ResponseEntity<InterviewDTO> addInterviewerToInterview(
            @RequestParam("interviewerId") int interviewerId,
            @RequestParam("interviewId") int interviewId,
            @Parameter(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization
    ) {
        InterviewDTO result = interviewService.addInterviewerToInterview(interviewId, interviewerId, authorization);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping("updateLanguageScore")
    public ResponseEntity<Object> updateLanguageScore(@RequestParam int interviewId,
                                                      @RequestParam int recruiterId,
                                                      @RequestBody float languageScore){
        return new ResponseEntity<>(interviewService.updateLanguageScore(interviewId, recruiterId, languageScore), HttpStatus.OK);
    }

    @PutMapping("updateSoftScore")
    public ResponseEntity<Object> updateSoftScore(@RequestParam int interviewId,
                                                      @RequestParam int recruiterId,
                                                      @RequestBody float softSkillScore){
        return new ResponseEntity<>(interviewService.updateSoftScore(interviewId, recruiterId, softSkillScore), HttpStatus.OK);
    }

    @GetMapping("/{recruiterId}")
    public ResponseEntity<List<InterviewDTO>> getInterviewByRecruiter(@PathVariable Integer recruiterId) {
        List<InterviewDTO> interviews = interviewService.getInterviewByRecruiterId(recruiterId);
        return new ResponseEntity<>(interviews, HttpStatus.OK);
    }

    @PutMapping("updateStatus")
    public ResponseEntity<InterviewDTO> updateStatus(@RequestParam int interviewId,
                                                     @RequestParam int recruiterId,
                                                     @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime interviewDatetime){
        if (interviewDatetime != null) {
            return new ResponseEntity<>(interviewService.updateStatus(interviewId, recruiterId, interviewDatetime), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(interviewService.updateStatusWithNull(interviewId, recruiterId), HttpStatus.OK);
        }

    }

    @GetMapping("/cv/{interviewId}")
    public ResponseEntity<CvDTO> getCv(@PathVariable Integer interviewId, @Parameter(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        return new ResponseEntity<>(cvService.getCv(interviewId, authorization), HttpStatus.OK);
    }
    @GetMapping("completed")
    public ResponseEntity<Object> getCompletedInterviewes(@Parameter(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization)
    {
        return new ResponseEntity<>(interviewService.getCompletedInterviewes(authorization),HttpStatus.OK);
    }
    @GetMapping("position/{positionId}")
    public ResponseEntity<List<InterviewDTO>> getInterviewsByPositionByRecruiter(@Parameter(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,@PathVariable Integer positionId)
    {
        return  new ResponseEntity<>(interviewService.getInterviewsByPosition(authorization,positionId),HttpStatus.OK);
    }
    @PutMapping("/updateInterviewerOrDateTime")
    public ResponseEntity<InterviewDTO> updateInterview(@RequestBody InterviewDTO interviewDTO,
                                                        @RequestParam Integer interviewId,
                                                        @Parameter(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization
                                                        )
    {
        return  new ResponseEntity<>(interviewService.updateInterviewerOrDateTime(interviewDTO,interviewId,authorization),HttpStatus.OK);
    }

    @PutMapping("/completes")
    public ResponseEntity<InterviewDTO> updateCompleteRecruiter(@RequestParam int interviewId,
                                                                @RequestParam String summary,
                                                                @Parameter(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        return new ResponseEntity<>(interviewService.updateRecruiterStatus(interviewId, summary, authorization), HttpStatus.OK);
    }
}
