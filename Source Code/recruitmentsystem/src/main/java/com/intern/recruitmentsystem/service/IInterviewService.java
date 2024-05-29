package com.fpt.recruitmentsystem.service;

import com.fpt.recruitmentsystem.model.Interview;
import com.fpt.recruitmentsystem.dto.CandidateVacancyDTO;
import com.fpt.recruitmentsystem.dto.InterviewDTO;
import com.fpt.recruitmentsystem.dto.InterviewQuestionDTO;
import com.fpt.recruitmentsystem.dto.RecruiterDTO;
import com.google.api.services.calendar.model.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface IInterviewService {
    RecruiterDTO getRecruiterById(Integer id);
    void updateInterviewerScore(Integer interviewId);
    InterviewQuestionDTO addQuestionToInterview(Integer interviewId, Integer questionId );
    void deleteQuestionFromInterview(Integer interviewId, Integer questionId );
    InterviewQuestionDTO updateQuestionToInterview(Integer interviewId, Integer questionId, Integer newquestionId );
    InterviewQuestionDTO giveScoreByInterviewer(Integer interviewId, Integer questionId, InterviewQuestionDTO resultInterview);
    InterviewDTO addInterview (InterviewDTO interviewDTO,String authorizationHeader);
    Event createEvent(String code, Interview interview);
    Interview findInterviewById(int id);
    Interview getInterviewByIdUnique(int id);
    List<InterviewDTO> getAllInterview(Integer page, Integer limit, Integer vacancyId);
    String getEmailInterviewer(Interview interview);
    String getEmailRecruiter(int recruiterId);
    InterviewDTO addInterviewerToInterview(int interviewId, int interviewerId, String authorizationHeader);
    InterviewDTO updateLanguageScore(int interviewId, int recruiterId, float languageScore);
    String getEmailCandidate(Interview interview);
    InterviewDTO updateSoftScore(int interviewId, int recruiterId, float softScore);
    InterviewDTO updateStatusWithNull(int interviewId, int recruiterId);
    CandidateVacancyDTO approve(Integer interviewId);
    CandidateVacancyDTO reject(Integer interviewId);
    List<InterviewDTO> getInterviewsByInterviewer(String authorization);
    InterviewDTO updateStatus(int interviewId, int recruiterId, LocalDateTime dateTime);
    List<InterviewDTO> getInterviewByRecruiterId(Integer interviewId);
    InterviewDTO getInterviewById(Integer id);
    InterviewDTO getInterviewByInterviewer(String authorization, Integer interviewId );
    List<InterviewDTO> getInterviewHistoryOfCandidate(String authorization);
    InterviewDTO getInterviewByCandidate(Integer interviewId, String authorization);
    List<InterviewDTO> getCompletedInterviewes(String authorization);
    List<InterviewDTO> getInterviewsByPosition(String authorizationHeader,Integer positionId);
    InterviewDTO updateInterviewerOrDateTime(InterviewDTO interviewDTO,Integer id, String authorizationHeader);

    Boolean checkFreeInterviewer(int interviewerId, List<Integer> dateTime);

    InterviewDTO updateInterviewerStatus(Integer interviewId, String authorizationHeader);

    InterviewDTO updateRecruiterStatus(Integer interviewId, String summary, String authorizationHeader);
    InterviewDTO updateLinkGoogleMeet(Integer interviewId, String linkGoogleMeet);
}
