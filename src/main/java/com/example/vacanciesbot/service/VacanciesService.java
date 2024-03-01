package com.example.vacanciesbot.service;

import java.util.List;
import java.util.Map;

public interface VacanciesService {

    Map<String, String> getOldVacancies(String candidateId);
    Map<String, String> getNewVacancies(String candidateId);
    String responseOnVacancies(String candidateId, Map<String, List<String>> vacancies, String count);
}
