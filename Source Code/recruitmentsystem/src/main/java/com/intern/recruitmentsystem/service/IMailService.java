package com.fpt.recruitmentsystem.service;

import com.fpt.recruitmentsystem.dto.CandidateDTO;
import com.fpt.recruitmentsystem.dto.CollectedCandidateDTO;
import com.fpt.recruitmentsystem.model.Event;
import com.fpt.recruitmentsystem.model.Vacancy;

public interface IMailService {
    void sendResetPasswordLink(String email, String link);
    void sendActiveAccountLink(String email, String link);
    void sendApplyEventSuccess(Event event, CollectedCandidateDTO collectedCandidateDTO);
    void sendApplyVacancySuccess(Vacancy vacancy, CandidateDTO candidateDTO);
    void sendLoginGoogleSuccess(String email, String firstName, String lastName);
}
