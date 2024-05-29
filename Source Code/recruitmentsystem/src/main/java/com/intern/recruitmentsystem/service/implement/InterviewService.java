package com.fpt.recruitmentsystem.service.implement;


import com.fpt.recruitmentsystem.exception.ForbiddenException;
import com.fpt.recruitmentsystem.model.*;
import com.fpt.recruitmentsystem.service.IAccountService;
import com.fpt.recruitmentsystem.service.IInterviewService;
import com.fpt.recruitmentsystem.util.Message;
import com.fpt.recruitmentsystem.util.Utility;
import com.fpt.recruitmentsystem.dto.*;
import com.fpt.recruitmentsystem.enumeration.InterviewStatus;
import com.fpt.recruitmentsystem.exception.BadRequestException;
import com.fpt.recruitmentsystem.exception.NotAcceptableException;
import com.fpt.recruitmentsystem.exception.NotFoundException;
import com.fpt.recruitmentsystem.mapper.*;
import com.fpt.recruitmentsystem.repository.*;

import com.google.api.services.calendar.model.Event;

import lombok.RequiredArgsConstructor;


import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Service
@RequiredArgsConstructor
public class InterviewService implements IInterviewService {
    private final RecruiterRepository recruiterRepository;
    private final InterviewQuesionRepository interviewQuestionRepository;
    private final InterviewRepository interviewRepository;
    private final InterviewerRepository interviewerRepository;
    private final QuestionRepository questionRepository;
    private final RecruiterMapper recruiterMapper;
    private final InterviewQuestionMapper interviewQuestionMapper;
    private final InterviewMapper interviewMapper;
    private final CandidateRepository candidateRepository;
    private final CandidateVacancyRepository candidateVacancyRepository;
    private final CandidateVacancyMapper candidateVacancyMapper;
    private final IAccountService accountService;
    private final VacancyRepository vacancyRepository;
    private final CandidateMapper candidateMapper;
    private final AuthCalendarService authCalendarService;
    private String dateType = "yyyy-MM-dd'T'HH:mm:ss";

    public RecruiterDTO getRecruiterById(Integer id) {
        Recruiter recruiterOptional = recruiterRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Message.RECRUITER_NOT_FOUND));

        return recruiterMapper.mapToDTO(recruiterOptional);
    }

    public void updateInterviewerScore(Integer interviewId) {
        List<InterviewQuestion> interviewQuestions = interviewQuestionRepository.findAllByInterviewId(interviewId);
        int count = interviewQuestions.size();
        float totalScore = 0;

        for (InterviewQuestion question : interviewQuestions) {
            Float questionScore = question.getScore();
            totalScore += (questionScore != null) ? questionScore : 0;
        }

        totalScore /= (count > 0) ? count : 1;

        Interview interview = interviewRepository.findById(interviewId).orElse(null);
        if (interview != null) {
            interview.setInterviewerScore(totalScore);
            interviewRepository.save(interview);
        }
    }

    public InterviewQuestionDTO giveScoreByInterviewer(Integer interviewId, Integer questionId, InterviewQuestionDTO resultInterview) {
        InterviewQuestion interviewQuestion = interviewQuestionRepository.findInterviewQuestionsByInterviewIdAndQuestionId(interviewId, questionId);
        if (interviewQuestion == null) {
            throw new NotFoundException("Question with id " + questionId + " is not accompanied with Interview id " + interviewId);
        }

        InterviewQuestion updateResult = interviewQuestionMapper.mapToEntity(resultInterview);

        if (updateResult.getScore() <= 10 && updateResult.getScore() >= 0) {
            interviewQuestion.setScore(updateResult.getScore());
            interviewQuestion.setNote(updateResult.getNote());
            updateInterviewerScore(interviewId);

            updateResult = interviewQuestionRepository.save(interviewQuestion);
            resultInterview = interviewQuestionMapper.mapToDTO(updateResult);
            return resultInterview;
        } else {
            throw new NotAcceptableException("Score must be less than 10 and greater than 0!");
        }
    }

    @Override
    public InterviewDTO addInterview(InterviewDTO interviewDTO, String authorizationHeader) {
        try {
            Recruiter recruiter = accountService.getRecruiterByAuthorizationHeader(authorizationHeader);
            int recruiterid = recruiter.getId();
            List<Integer> listVacancyId = recruiterRepository.getVacancyByRecruiter(recruiterid);
            int vacancyid = interviewDTO.getCandidateVacancy().getVacancy().getId();
            if (listVacancyId.contains(vacancyid)) {
                Interview interviewEntity = interviewMapper.mapToEntity(interviewDTO);

                List<Integer> datetimeList = new ArrayList<>();
                datetimeList.add(interviewEntity.getInterviewDatetime().getYear());
                datetimeList.add(interviewEntity.getInterviewDatetime().getMonthValue());
                datetimeList.add(interviewEntity.getInterviewDatetime().getDayOfMonth());
                datetimeList.add(interviewEntity.getInterviewDatetime().getHour());
                datetimeList.add(interviewEntity.getInterviewDatetime().getMinute());
                datetimeList.add(interviewEntity.getInterviewDatetime().getSecond());

                if (!Objects.isNull(interviewDTO.getInterviewer())) {
                    Integer interviewerid = interviewDTO.getInterviewer().getId();
                    if (interviewerid != null) {
                        if (Boolean.TRUE.equals(checkFreeInterviewer(interviewerid, datetimeList))) {
                            Interviewer interviewer = interviewerRepository.findInterviewerById(interviewerid);
                            interviewEntity.setInterviewer(interviewer);
                        } else {
                            throw new BadRequestException(Message.CONFLICT_SCHEDULE_INTERVIEWER);
                        }
                    } else {
                        interviewEntity.setInterviewer(null);
                    }
                }

                interviewEntity.setRecruiter(recruiter);
                interviewEntity.setInterviewerScore((float) 0);
                interviewEntity.setLanguageSkillScore((float) 0);
                interviewEntity.setSoftSkillScore((float) 0);
                interviewEntity.setTotalScore((float) 0);
                interviewEntity.setStatus(InterviewStatus.PENDING);
                interviewEntity.setInterviewerStatus(Boolean.FALSE);
                interviewEntity.setRecruiterStatus(Boolean.FALSE);
                Interview interview = interviewRepository.save(interviewEntity);

                return interviewMapper.mapToDTO(interview);
            } else {
                throw new BadRequestException("Recruiter doest not permission for create interview for vacancy " + vacancyid);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BadRequestException("Can't not create interview " + e);
        }
    }

    @Override
    public Event createEvent(String code, Interview interview) {
        List<String> emailList = new ArrayList<>();
        emailList.add(getEmailCandidate(interview));
        emailList.add(getEmailRecruiter(interview.getRecruiter().getId()));
        emailList.add(getEmailInterviewer(interview));
        LocalDateTime localDateTime = LocalDateTime.parse(interview.getInterviewDatetime().toString());

        return authCalendarService.createEvent(code, emailList, localDateTime);
    }

    @Override
    public Interview findInterviewById(int id) {
        return interviewRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Message.INTERVIEW_NOT_FOUND));
    }

    @Override
    public Interview getInterviewByIdUnique(int id) {
        return interviewRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Message.INTERVIEW_NOT_FOUND));
    }

    @Override
    public List<InterviewDTO> getAllInterview(Integer page, Integer limit, Integer vacancyId) {
        Pageable pageable = Utility.getPageable(page, limit);
        Specification<Interview> spec = createInterviewSpecification(vacancyId);
        List<Interview> interviews = interviewRepository.findAll(spec, pageable);
        return interviews.stream().
                map(interviewMapper::mapToDTO).toList();
    }

    @Override
    public String getEmailInterviewer(Interview interview) {
        String emailInterviewr = "recruitmentsystem08@gmail.com";
        if (interview.getInterviewer() != null) {
            emailInterviewr = interviewerRepository.getEmailInterviewr(interview.getInterviewer().getId());
        }
        return emailInterviewr;
    }

    @Override
    public String getEmailRecruiter(int recruiterId) {
        return recruiterRepository.getEmailRecruiter(recruiterId);
    }

    @Override
    public String getEmailCandidate(Interview interview) {
        return candidateRepository.findEmailCandidateByCandidateVacancy(interview.getCandidateVacancy().getId());
    }

    @Override
    public InterviewDTO addInterviewerToInterview(int interviewId, int interviewerId, String authorizationHeader) {
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new NotFoundException(Message.INTERVIEW_NOT_FOUND));
        Interviewer interviewer = interviewerRepository.findInterviewerById(interviewerId);
        Recruiter recruiter = accountService.getRecruiterByAuthorizationHeader(authorizationHeader);
        int recruiterid = recruiter.getId();
        List<Integer> listVacancyId = recruiterRepository.getVacancyByRecruiter(recruiterid);
        int vacancyid = interview.getCandidateVacancy().getVacancy().getId();
        if (listVacancyId.contains(vacancyid)) {
            List<Integer> datetimeList = new ArrayList<>();
            datetimeList.add(interview.getInterviewDatetime().getYear());
            datetimeList.add(interview.getInterviewDatetime().getMonthValue());
            datetimeList.add(interview.getInterviewDatetime().getDayOfMonth());
            datetimeList.add(interview.getInterviewDatetime().getHour());
            datetimeList.add(interview.getInterviewDatetime().getMinute());
            datetimeList.add(interview.getInterviewDatetime().getSecond());

            if (Boolean.TRUE.equals(checkFreeInterviewer(interviewerId, datetimeList))) {
                interview.setInterviewer(interviewer);
                Interview interview1 = interviewRepository.save(interview);

                return interviewMapper.mapToDTO(interview1);
            } else {
                throw new BadRequestException("Can't not add interviewer to interview because conflict schedule with intervier id" + interviewerId);
            }
        } else {
            throw new BadRequestException("Recruiter does not permisson for interview " + interview.getId());
        }
    }

    @Override
    public InterviewDTO updateLanguageScore(int interviewId, int recruiterId, float languageScore) {
        Interview interview = interviewRepository.findInterviewByRecruiterIdAndInterviewId(interviewId, recruiterId)
                .orElseThrow(() -> new NotFoundException(Message.INTERVIEW_NOT_FOUND));
        interview.setLanguageSkillScore(languageScore);

        Interview interview1 = interviewRepository.save(interview);
        return interviewMapper.mapToDTO(interview1);
    }

    @Override
    public InterviewDTO updateSoftScore(int interviewId, int recruiterId, float softScore) {
        Interview interview = interviewRepository.findInterviewByRecruiterIdAndInterviewId(interviewId, recruiterId)
                .orElseThrow(() -> new NotFoundException(Message.INTERVIEW_NOT_FOUND));
        interview.setSoftSkillScore(softScore);

        Interview interview1 = interviewRepository.save(interview);
        return interviewMapper.mapToDTO(interview1);
    }

    @Override
    public InterviewDTO updateStatus(int interviewId, int recruiterId, LocalDateTime interviewDatetime) {
        if (interviewDatetime.isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Interview time before current time");
        }
        Interview interview = interviewRepository.findInterviewByRecruiterIdAndInterviewId(interviewId, recruiterId)
                .orElseThrow(() -> new NotFoundException(Message.INTERVIEW_NOT_FOUND));

        List<Integer> datetimeList = new ArrayList<>();
        datetimeList.add(interviewDatetime.getYear());
        datetimeList.add(interviewDatetime.getMonthValue());
        datetimeList.add(interviewDatetime.getDayOfMonth());
        datetimeList.add(interviewDatetime.getHour());
        datetimeList.add(interviewDatetime.getMinute());
        datetimeList.add(interviewDatetime.getSecond());

        if (!Objects.isNull(interview.getInterviewer())) {
            Integer interviewerId = interview.getInterviewer().getId();
            if (interviewerId != null) {
                if (Boolean.TRUE.equals(checkInterviewer(interviewerId, datetimeList))) {
                    if (Boolean.TRUE.equals(checkRecruiter(recruiterId, datetimeList))) {
                        interview.setStatus(InterviewStatus.PENDING);
                        interview.setInterviewDatetime(interviewDatetime);
                    } else {
                        throw new BadRequestException("Conflict schedule recruiter " + recruiterId);
                    }
                } else {
                    throw new BadRequestException(Message.CONFLICT_SCHEDULE_INTERVIEWER);
                }
            }
        } else {
            interview.setStatus(InterviewStatus.PENDING);
            interview.setInterviewDatetime(interviewDatetime);
        }
        Interview updatedInterview = interviewRepository.save(interview);
        return interviewMapper.mapToDTO(updatedInterview);

    }

    @Override
    public InterviewDTO updateStatusWithNull(int interviewId, int recruiterId) {
        Interview interview = interviewRepository.findInterviewByRecruiterIdAndInterviewId(interviewId, recruiterId)
                .orElseThrow(() -> new NotFoundException(Message.INTERVIEW_NOT_FOUND));
        interview.setStatus(InterviewStatus.CANCELLED);
        Interview interview1 = interviewRepository.save(interview);
        return interviewMapper.mapToDTO(interview1);
    }

    public Float updateTotalScore(Interview interview) {
        float totalScore;
        if (interview.getInterviewerScore() == null) {
            totalScore = (float) (interview.getLanguageSkillScore() * 0.5 + interview.getSoftSkillScore() * 0.5);
        } else {
            totalScore = (float) (interview.getInterviewerScore() * 0.6 + interview.getLanguageSkillScore() * 0.2
                    + interview.getSoftSkillScore() * 0.2);
        }
        return totalScore;
    }

    public InterviewQuestionDTO addQuestionToInterview(Integer interviewId, Integer questionId) {
        InterviewQuestion newInterviewQuestion = new InterviewQuestion();

        Interview i = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new NotFoundException(Message.INTERVIEW_NOT_FOUND));

        Question q = questionRepository.findById(questionId)
                .orElseThrow(() -> new NotFoundException(Message.QUESTION_NOT_FOUND));

        InterviewQuestionKey idk = new InterviewQuestionKey();
        idk.setQuestionId(questionId);
        idk.setInterviewId(interviewId);

        newInterviewQuestion.setQuestion(q);
        newInterviewQuestion.setInterview(i);
        newInterviewQuestion.setId(idk);

        InterviewQuestion saveInterviewQuestion = interviewQuestionRepository.save(newInterviewQuestion);
        return interviewQuestionMapper.mapToDTO(saveInterviewQuestion);
    }

    public void deleteQuestionFromInterview(Integer interviewId, Integer questionId) {
        InterviewQuestion deletedQuestion = interviewQuestionRepository.findInterviewQuestionsByInterviewIdAndQuestionId(interviewId, questionId);

        InterviewQuestionKey idk = new InterviewQuestionKey();
        idk.setInterviewId(deletedQuestion.getInterview().getId());
        idk.setQuestionId(deletedQuestion.getQuestion().getId());

        deletedQuestion.setId(idk);

        interviewQuestionRepository.deleteById(deletedQuestion.getId());
        updateInterviewerScore(interviewId);

    }

    @Override
    public InterviewQuestionDTO updateQuestionToInterview(Integer interviewId, Integer questionId, Integer newQuestionId) {
        InterviewQuestion oldInterviewQuestion = interviewQuestionRepository.findInterviewQuestionsByInterviewIdAndQuestionId(interviewId, questionId);
        Question nq = questionRepository.findById(newQuestionId)
                .orElseThrow(() -> new NotFoundException(Message.QUESTION_NOT_FOUND));

        InterviewQuestionKey newIdk = new InterviewQuestionKey();
        newIdk.setInterviewId(interviewId);
        newIdk.setQuestionId(newQuestionId);

        // Kiểm tra và xóa oldInterviewQuestion nếu đã tồn tại
        if (oldInterviewQuestion != null) {
            interviewQuestionRepository.delete(oldInterviewQuestion);
        }

        // Tạo đối tượng mới để thay thế
        InterviewQuestion newInterviewQuestion = new InterviewQuestion();
        newInterviewQuestion.setId(newIdk);
        newInterviewQuestion.setQuestion(nq);
        newInterviewQuestion.setNote(null);
        newInterviewQuestion.setScore(null);

        // Lấy đối tượng Interview tương ứng với interviewId
        Interview interview = findInterviewById(interviewId);

        // Gán đối tượng Interview vào newInterviewQuestion
        newInterviewQuestion.setInterview(interview);

        // Lưu newInterviewQuestion vào cơ sở dữ liệu
        InterviewQuestion savedQuestion = interviewQuestionRepository.save(newInterviewQuestion);
        updateInterviewerScore(interviewId);
        return interviewQuestionMapper.mapToDTO(savedQuestion);
    }

    public CandidateVacancyDTO approve(Integer interviewId) {
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new NotFoundException(Message.INTERVIEW_NOT_FOUND));
        CandidateVacancy candidateVacancy = interview.getCandidateVacancy();
        candidateVacancy.setStatus("PASS");
        CandidateVacancy updatedCandidateVacancy = candidateVacancyRepository.save(candidateVacancy);
        return candidateVacancyMapper.mapToDTO(updatedCandidateVacancy);
    }

    public CandidateVacancyDTO reject(Integer interviewId) {
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new NotFoundException(Message.INTERVIEW_NOT_FOUND));
        CandidateVacancy candidateVacancy = interview.getCandidateVacancy();
        candidateVacancy.setStatus("FAILED");
        CandidateVacancy updatedCandidateVacancy = candidateVacancyRepository.save(candidateVacancy);
        return candidateVacancyMapper.mapToDTO(updatedCandidateVacancy);
    }

    public List<InterviewDTO> getInterviewByRecruiterId(Integer recruiterId) {
        List<Interview> interviews = interviewRepository.findByRecruiterId(recruiterId);
        if (interviews.isEmpty()) {
            throw new NotFoundException(Message.INTERVIEW_NOT_FOUND);
        }
        return interviews.stream()
                .map(interviewMapper::mapToDTO)
                .toList();
    }

    public List<InterviewDTO> getInterviewsByInterviewer(String authorizationHeader) {
        Interviewer interviewer = accountService.getInterviewerByAuthorizationHeader(authorizationHeader);
        List<Interview> interviewList = interviewRepository.findInterviewsByInterviewerId(interviewer.getId());
        if (interviewList.isEmpty()) {
            throw new NotFoundException(Message.INTERVIEW_NOT_FOUND);
        }
        return interviewList.stream()
                .map(interviewMapper::mapToDTO)
                .toList();
    }

    @Override
    public InterviewDTO getInterviewById(Integer id) {
        Optional<Interview> interview = interviewRepository.findById(id);
        if (interview.isEmpty()) {
            throw new NotFoundException(Message.INTERVIEW_NOT_FOUND);
        }
        return interviewMapper.mapToDTO(interview.get());
    }

    public List<CandidateDTO> getListCandidateByVacancyIdInInterView(int id) {
        Vacancy vacancy = vacancyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Message.VACANCY_NOT_FOUND));

        List<Candidate> candidateList = vacancyRepository.findListCandidateByVacancyId(vacancy.getId());
        if (candidateList.isEmpty()) {
            throw new NotFoundException(Message.CANDIDATE_NOT_FOUND);
        }
        return candidateList.stream().
                map(candidateMapper::mapToDTO).
                toList();
    }

    public InterviewDTO getInterviewByInterviewer(String authorizationHeader, Integer interviewId) {
        List<InterviewDTO> listInterviews = getInterviewsByInterviewer(authorizationHeader);

        for (InterviewDTO interview : listInterviews) {
            if (interview.getId().equals(interviewId)) {
                return interview;
            }
        }

        throw new NotFoundException("Have Error with ID: " + interviewId);
    }

    public List<InterviewDTO> getInterviewHistoryOfCandidate(String authorization) {
        Candidate candidate = accountService.getCandidateByAuthorizationHeader(authorization);
        List<Interview> interviews = interviewRepository.findByCandidateVacancyCandidateId(candidate.getId());

        if (interviews.isEmpty()) {
            throw new NotFoundException(Message.INTERVIEW_NOT_FOUND);
        }

        return interviews.stream()
                .map(interviewMapper::mapToDTO)
                .toList();
    }

    public InterviewDTO getInterviewByCandidate(Integer interviewId, String authorization) {
        Candidate candidate = accountService.getCandidateByAuthorizationHeader(authorization);
        Set<CandidateVacancy> candidateVacancies = candidate.getCandidateVacancies();
        Optional<Interview> foundInterview = candidateVacancies.stream()
                .map(CandidateVacancy::getInterview)
                .filter(interview -> interview != null && interview.getId().equals(interviewId))
                .findFirst();

        return foundInterview.map(interviewMapper::mapToDTO)
                .orElseThrow(() -> new NotFoundException(Message.INTERVIEW_NOT_FOUND));
    }

    public List<InterviewDTO> getCompletedInterviewes(String authorizationHeader) {
        Recruiter recruiter = accountService.getRecruiterByAuthorizationHeader(authorizationHeader);
        List<InterviewDTO> listInterviews = getInterviewByRecruiterId(recruiter.getId());

        List<InterviewDTO> completedInterviews = listInterviews.stream()
                .filter(interview -> interview.getStatus() == InterviewStatus.COMPLETED)
                .toList();

        if (completedInterviews.isEmpty()) {
            throw new NotFoundException("Not Yet COMPLETED interviews ");
        } else
            return completedInterviews;
    }

    public List<InterviewDTO> getInterviewsByPosition(String authorizationHeader, Integer positionId) {
        Recruiter recruiter = accountService.getRecruiterByAuthorizationHeader(authorizationHeader);
        List<Interview> it = interviewRepository.findInterviewsByRecruiterAndPosition(recruiter.getId(), positionId);
        List<Interview> completedInterviews = it.stream()
                .filter(interview -> interview.getStatus() == InterviewStatus.COMPLETED)
                .toList();
        if (completedInterviews.isEmpty()) {
            throw new NotFoundException("Not Yet COMPLETED interviews with Position you Find!!");
        }
        return completedInterviews.stream()
                .map(interviewMapper::mapToDTO) // Chuyển đổi từ Interview sang InterviewDTO
                .toList();
    }

    @Override
    public Boolean checkFreeInterviewer(int interviewerId, List<Integer> dateTime) {
        List<Interview> interviewList = interviewRepository.findInterviewsByInterviewerId(interviewerId);
        List<LocalDateTime> listDatetime = new ArrayList<>();
        for (int i = 0; i < interviewList.size(); i++) {
            listDatetime.add(interviewList.get(i).getInterviewDatetime());
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateType);
        String month = dateTime.get(1).toString();
        String day = dateTime.get(2).toString();
        String gio = dateTime.get(3).toString();
        String phut = dateTime.get(4).toString();
        String giay = dateTime.get(5).toString();

        if (dateTime.get(5) < 10) {
            giay = "0" + dateTime.get(5).toString();
        }
        if (dateTime.get(1) < 10) {
            month = "0" + dateTime.get(1).toString();
        }
        if (dateTime.get(2) < 10) {
            day = "0" + dateTime.get(2).toString();
        }
        if (dateTime.get(3) < 10) {
            gio = "0" + dateTime.get(3).toString();
        }
        if (dateTime.get(4) < 10) {
            phut = "0" + dateTime.get(4).toString();
        }

        String dateTimeString = dateTime.get(0).toString() + "-" + month + "-" + day
                + "T" + gio + ":" + phut + ":" + giay;
        LocalDateTime startDateNew = LocalDateTime.parse(dateTimeString, formatter);

        for (int i = 0; i < listDatetime.size(); i++) {
            LocalDateTime endDate = listDatetime.get(i).plusHours(1);
            if (startDateNew.getDayOfMonth() == endDate.getDayOfMonth() && startDateNew.isBefore(endDate)
                    && startDateNew.isAfter(listDatetime.get(i))) {
                return false;
            }
        }
        return true;
    }

    public Boolean checkInterviewer(int interviewerId, List<Integer> dateTime) {
        List<Interview> interviewList = interviewRepository.findInterviewsByInterviewerId(interviewerId);
        List<LocalDateTime> listDatetime = new ArrayList<>();
        for (int i = 0; i < interviewList.size(); i++) {
            listDatetime.add(interviewList.get(i).getInterviewDatetime());
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateType);
        String month = dateTime.get(1).toString();
        String day = dateTime.get(2).toString();
        String gio = dateTime.get(3).toString();
        String phut = dateTime.get(4).toString();
        String giay = dateTime.get(5).toString();

        if (dateTime.get(5) < 10) {
            giay = "0" + dateTime.get(5).toString();
        }
        if (dateTime.get(1) < 10) {
            month = "0" + dateTime.get(1).toString();
        }
        if (dateTime.get(2) < 10) {
            day = "0" + dateTime.get(2).toString();
        }
        if (dateTime.get(3) < 10) {
            gio = "0" + dateTime.get(3).toString();
        }
        if (dateTime.get(4) < 10) {
            phut = "0" + dateTime.get(4).toString();
        }

        String dateTimeString = dateTime.get(0).toString() + "-" + month + "-" + day
                + "T" + gio + ":" + phut + ":" + giay;
        LocalDateTime startDateNew = LocalDateTime.parse(dateTimeString, formatter);

        for (LocalDateTime existingDatetime : listDatetime) {
            LocalDateTime existingEndDate = existingDatetime.plusMinutes(59);
            LocalDateTime endDateNew = startDateNew.plusMinutes(59);
            if (startDateNew.isEqual(existingDatetime) || startDateNew.isEqual(existingEndDate)) {
                return false;  // Xảy ra xung đột khi bắt đầu hoặc kết thúc trùng với một thời điểm có sẵn
            }
            if (startDateNew.isAfter(existingDatetime) && startDateNew.isBefore(existingEndDate)) {
                return false;  // Xảy ra xung đột khi thời điểm mới bắt đầu sau thời điểm có sẵn và trước thời điểm kết thúc của thời điểm có sẵn
            }
            if (endDateNew.isAfter(existingDatetime) && endDateNew.isBefore(existingEndDate)) {
                return false;  // Xảy ra xung đột khi thời điểm mới kết thúc sau thời điểm có sẵn và trước thời điểm kết thúc của thời điểm có sẵn
            }
            if (startDateNew.isBefore(existingDatetime) && endDateNew.isAfter(existingEndDate)) {
                return false;  // Xảy ra xung đột khi thời điểm mới bắt đầu trước thời điểm có sẵn và kết thúc sau thời điểm kết thúc của thời điểm có sẵn
            }
        }

        return true;  // Không có xung đột
    }

    public Boolean checkRecruiter(int recruiterId, List<Integer> dateTime) {
        List<Interview> interviewList = interviewRepository.findByRecruiterId(recruiterId);
        List<LocalDateTime> listDatetime = new ArrayList<>();
        for (int i = 0; i < interviewList.size(); i++) {
            listDatetime.add(interviewList.get(i).getInterviewDatetime());
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateType);
        String month = dateTime.get(1).toString();
        String day = dateTime.get(2).toString();
        String gio = dateTime.get(3).toString();
        String phut = dateTime.get(4).toString();
        String giay = dateTime.get(5).toString();

        if (dateTime.get(5) < 10) {
            giay = "0" + dateTime.get(5).toString();
        }
        if (dateTime.get(1) < 10) {
            month = "0" + dateTime.get(1).toString();
        }
        if (dateTime.get(2) < 10) {
            day = "0" + dateTime.get(2).toString();
        }
        if (dateTime.get(3) < 10) {
            gio = "0" + dateTime.get(3).toString();
        }
        if (dateTime.get(4) < 10) {
            phut = "0" + dateTime.get(4).toString();
        }

        String dateTimeString = dateTime.get(0).toString() + "-" + month + "-" + day
                + "T" + gio + ":" + phut + ":" + giay;
        LocalDateTime startDateNew = LocalDateTime.parse(dateTimeString, formatter);

        for (LocalDateTime existingDatetime : listDatetime) {
            LocalDateTime existingEndDate = existingDatetime.plusMinutes(59);
            LocalDateTime endDateNew = startDateNew.plusMinutes(59);
            if (startDateNew.isEqual(existingDatetime) || startDateNew.isEqual(existingEndDate)) {
                return false;  // Xảy ra xung đột khi bắt đầu hoặc kết thúc trùng với một thời điểm có sẵn
            }
            if (startDateNew.isAfter(existingDatetime) && startDateNew.isBefore(existingEndDate)) {
                return false;  // Xảy ra xung đột khi thời điểm mới bắt đầu sau thời điểm có sẵn và trước thời điểm kết thúc của thời điểm có sẵn
            }
            if (endDateNew.isAfter(existingDatetime) && endDateNew.isBefore(existingEndDate)) {
                return false;  // Xảy ra xung đột khi thời điểm mới kết thúc sau thời điểm có sẵn và trước thời điểm kết thúc của thời điểm có sẵn
            }
            if (startDateNew.isBefore(existingDatetime) && endDateNew.isAfter(existingEndDate)) {
                return false;  // Xảy ra xung đột khi thời điểm mới bắt đầu trước thời điểm có sẵn và kết thúc sau thời điểm kết thúc của thời điểm có sẵn
            }
        }

        return true;  // Không có xung đột
    }

    private static Specification<Interview> createInterviewSpecification(Integer vacancyId) {
        return (root, query, criteriaBuilder) -> {
            if (vacancyId != null) {
                return criteriaBuilder.equal(root.get("candidateVacancy").get("vacancy").get("id"), vacancyId);
            }
            return null; // Return null when vacancyId is null to get all interviews.
        };
    }

    public InterviewDTO updateInterviewerOrDateTime(InterviewDTO interviewDTO, Integer id, String authorizationHeader) {
        Recruiter recruiter = accountService.getRecruiterByAuthorizationHeader(authorizationHeader);
        Interview interview = interviewRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Message.INTERVIEW_NOT_FOUND));
        int recruiterid = recruiter.getId();
        List<Integer> listVacancyId = recruiterRepository.getVacancyByRecruiter(recruiterid);
        int vacancyid = interview.getCandidateVacancy().getVacancy().getId();

        if (listVacancyId.contains(vacancyid)) {
            List<Integer> datetimeList = new ArrayList<>();
            datetimeList.add(interviewDTO.getInterviewDatetime().getYear());
            datetimeList.add(interviewDTO.getInterviewDatetime().getMonthValue());
            datetimeList.add(interviewDTO.getInterviewDatetime().getDayOfMonth());
            datetimeList.add(interviewDTO.getInterviewDatetime().getHour());
            datetimeList.add(interviewDTO.getInterviewDatetime().getMinute());
            datetimeList.add(interviewDTO.getInterviewDatetime().getSecond());

            if (!Objects.isNull(interviewDTO.getInterviewer())) {
                Integer interviewerid = interviewDTO.getInterviewer().getId();
                if (interviewerid != null) {
                    if (Boolean.TRUE.equals(checkFreeInterviewer(interviewerid, datetimeList))) {
                        interview.setInterviewDatetime(interviewDTO.getInterviewDatetime());
                    } else {
                        throw new BadRequestException(Message.CONFLICT_SCHEDULE_INTERVIEWER);
                    }
                }
                Optional.ofNullable(interviewDTO.getInterviewer())
                        .map(interviewerDTO -> interviewerRepository.findInterviewerById(interviewerDTO.getId()))
                        .ifPresent(interview::setInterviewer);
            } else {
                if (!Objects.isNull(interview.getInterviewer())) {
                    Integer interviewerid = interview.getInterviewer().getId();
                    if (interviewerid != null) {
                        if (Boolean.TRUE.equals(checkFreeInterviewer(interviewerid, datetimeList))) {
                            interview.setInterviewDatetime(interviewDTO.getInterviewDatetime());
                        } else {
                            throw new BadRequestException(Message.CONFLICT_SCHEDULE_INTERVIEWER);
                        }
                    }
                } else {
                    interview.setInterviewDatetime(interviewDTO.getInterviewDatetime());
                }

            }

            Interview updatedInterview = interviewRepository.save(interview);
            return interviewMapper.mapToDTO(updatedInterview);
        } else {
            throw new BadRequestException("Recruiter doest not permission for create interview for vacancy " + vacancyid);
        }
    }

    @Override
    public InterviewDTO updateInterviewerStatus(Integer interviewId, String authorizationHeader) {
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new NotFoundException(Message.INTERVIEW_NOT_FOUND));
        Interviewer interviewer = accountService.getInterviewerByAuthorizationHeader(authorizationHeader);
        if (!interviewer.getId().equals(interview.getInterviewer().getId())) {
            throw new ForbiddenException(Message.ACCESS_DENIED);
        }
        interview.setInterviewerStatus(Boolean.TRUE);
        if (interview.getRecruiterStatus() == Boolean.TRUE) {
            interview.setStatus(InterviewStatus.COMPLETED);
            interview.setTotalScore(updateTotalScore(interview));
        }
        return interviewMapper.mapToDTO(interviewRepository.save(interview));
    }

    @Override
    public InterviewDTO updateRecruiterStatus(Integer interviewId, String summary, String authorizationHeader) {
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new NotFoundException(Message.INTERVIEW_NOT_FOUND));
        Recruiter recruiter = accountService.getRecruiterByAuthorizationHeader(authorizationHeader);
        if (!recruiter.getId().equals(interview.getRecruiter().getId())) {
            throw new ForbiddenException(Message.ACCESS_DENIED);
        }
        interview.setRecruiterStatus(Boolean.TRUE);
        interview.setSummary(summary);
        if (interview.getInterviewerStatus() == Boolean.TRUE) {
            interview.setStatus(InterviewStatus.COMPLETED);
            interview.setTotalScore(updateTotalScore(interview));
        }
        if (interview.getInterviewer() == null) {
            interview.setStatus(InterviewStatus.COMPLETED);
            interview.setTotalScore(updateTotalScore(interview));
        }
        return interviewMapper.mapToDTO(interviewRepository.save(interview));
    }

    @Override
    public InterviewDTO updateLinkGoogleMeet(Integer interviewId, String linkGoogleMeet) {
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new NotFoundException(Message.INTERVIEW_NOT_FOUND));
        interview.setLinkMeet(linkGoogleMeet);
        return interviewMapper.mapToDTO(interviewRepository.save(interview));
    }
}

