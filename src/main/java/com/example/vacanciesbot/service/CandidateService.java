package com.example.vacanciesbot.service;

import com.example.vacanciesbot.entity.Candidate;

public interface CandidateService {
    void update();
    Candidate findByNameAndLastname(String name, String lastname);
}
