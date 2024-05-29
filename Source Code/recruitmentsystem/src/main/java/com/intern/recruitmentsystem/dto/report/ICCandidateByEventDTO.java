package com.fpt.recruitmentsystem.dto.report;

public interface ICCandidateByEventDTO {
    int getCollectedCandidates();

    int getEvents();

    void setCollectedCandidates(int collectedCandidates);

    void setEvents(int events);
}
