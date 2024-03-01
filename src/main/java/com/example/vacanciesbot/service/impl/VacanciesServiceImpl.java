package com.example.vacanciesbot.service.impl;

import com.example.vacanciesbot.contracts.input.hh.CreateHHAuthCodeUseCase;
import com.example.vacanciesbot.dto.request.*;
import com.example.vacanciesbot.dto.request.api.IOperator;
import com.example.vacanciesbot.dto.response.Item;
import com.example.vacanciesbot.dto.response.NotionResponseDTO;
import com.example.vacanciesbot.dto.response.Page;
import com.example.vacanciesbot.dto.response.VacancyResponseDto;
import com.example.vacanciesbot.entity.Candidate;
import com.example.vacanciesbot.repository.CandidateRepository;
import com.example.vacanciesbot.service.VacanciesService;
import lombok.RequiredArgsConstructor;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
public class VacanciesServiceImpl implements VacanciesService {

    private final CandidateRepository candidateRepository;
    private final CreateHHAuthCodeUseCase createHHAuthCodeUseCase;

    private final String token = "secret_bhPmG73SmJHyOClvh33cRPhw9jF5z2xYQtOeMaICiZR";
    private final String urlInterview = "https://api.notion.com/v1/databases/dd6981d34c4e4e86b7ba83b364d081a8/query";

    @Value("${web.api.baseaddress}")
    private String baseAddress;
    private final String notionVersion = "2022-06-28";

    @Override
    @Transactional
    public Map<String, String> getOldVacancies(String candidateId) {
        Candidate candidate = candidateRepository.findById(Long.valueOf(candidateId)).orElseThrow();

        Map<String, String> oldVacancies = new HashMap<>();

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().add("Authorization", "Bearer " + token);
            request.getHeaders().add("Notion-Version", notionVersion);
            return execution.execute(request, body);
        });

        NotionRequestDTO requestDTO = new NotionRequestDTO();
        FilterOr filterOr = new FilterOr();
        List<IOperator> or = new ArrayList<>();
        or.add(new OperatorSelect("uX%600", new Status("Тех собес")));
        or.add(new OperatorSelect("uX%600", new Status("Скрининг")));
        filterOr.setOr(or);
        requestDTO.setFilter(filterOr);

        ResponseEntity<NotionResponseDTO> responseEntity = restTemplate.postForEntity(urlInterview,
                requestDTO, NotionResponseDTO.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            for (Page result : responseEntity.getBody().getResults()) {
                try {
                    String company = result.getProperties().get("Company").getRich_text().get(0).getPlain_text();
                    String position = result.getProperties().get("Направление").getSelect().getName();
                    String hhUrl = result.getProperties().get("Ссылка HH").getUrl();

                    if (position.equalsIgnoreCase(candidate.getPosition()) && hhUrl != null) {
                        if (!oldVacancies.containsValue(hhUrl)) {
                            oldVacancies.put(company, hhUrl);
                        }
                    }

                } catch (Exception e) {
                    e.getStackTrace();
                }
            }

            return oldVacancies;
        } else {
            throw new RuntimeException(responseEntity.getStatusCode().toString());
        }
    }

    @Override
    public Map<String, String> getNewVacancies(String candidateId) {
        Candidate candidate = candidateRepository.findById(Long.valueOf(candidateId)).orElseThrow();

        Map<String, String> newVacancies = new HashMap<>();

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().add("Authorization", "Bearer " + token);
            request.getHeaders().add("Notion-Version", notionVersion);
            return execution.execute(request, body);
        });

        NotionRequestDTO requestDTO = new NotionRequestDTO();
        FilterOr filterOr = new FilterOr();
        List<IOperator> or = new ArrayList<>();
        or.add(new OperatorSelect("uX%600", new Status("Тех собес")));
        or.add(new OperatorSelect("uX%600", new Status("Скрининг")));
        filterOr.setOr(or);
        requestDTO.setFilter(filterOr);

        ResponseEntity<NotionResponseDTO> responseEntity = restTemplate.postForEntity(urlInterview,
                requestDTO, NotionResponseDTO.class);

        RestTemplate restTemplateHH = new RestTemplate();

        ResponseEntity<VacancyResponseDto> vacancyEntity = restTemplateHH.getForEntity(baseAddress + "/vacancies?text=\"" + candidate.getPosition() + "+разработчик\" + &area=113&per_page=100"
                , VacancyResponseDto.class);

        RestTemplate restTemplateVacancy = new RestTemplate();

        restTemplateVacancy.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().add("Authorization", "Bearer " + candidate.getAccess());
            return execution.execute(request, body);
        });

        ResponseEntity<VacancyResponseDto> negotiationsVacancy = restTemplateVacancy.getForEntity(baseAddress + "/negotiations"
                , VacancyResponseDto.class);

        if (responseEntity.getStatusCode().is2xxSuccessful() && vacancyEntity.getStatusCode().is2xxSuccessful() && negotiationsVacancy.getStatusCode().is2xxSuccessful()) {

            List<Page> results = responseEntity.getBody().getResults();

            List<Item> items = negotiationsVacancy.getBody().getItems();

            vacancyEntity.getBody().getItems().stream()
                    .filter(vacancy -> results.stream()
                            .filter(result -> result.getProperties().get("Ссылка HH").getUrl() != null)
                            .noneMatch(result -> result.getProperties().get("Ссылка HH").getUrl().equals(vacancy.getAlternate_url()))
                            && items.stream()
                            .noneMatch(item -> item.getVacancy().getAlternate_url().equals(vacancy.getAlternate_url()))
                    )
                    .limit(10)
                    .toList()
                    .forEach(item -> newVacancies.put(item.getEmployer().getName(), item.getAlternate_url()));

            return newVacancies;
        } else {
            throw new RuntimeException(responseEntity.getStatusCode().toString());
        }
    }

    @Override
    public String responseOnVacancies(String candidateId, Map<String, List<String>> vacancies, String count) {

        String state = createHHAuthCodeUseCase.checkExpired(Long.valueOf(candidateId));

        if (state.equals("NotAuth")) {
            return "NotAuth";
        }

        Candidate candidate = candidateRepository.findById(Long.valueOf(candidateId)).orElseThrow();

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().add("Authorization", "Bearer " + candidate.getAccess());
            return execution.execute(request, body);
        });

        List<String> vacanciesOnResponse = vacancies.get(candidateId);

        if (count.equalsIgnoreCase("Все")) {
            vacanciesOnResponse.forEach(
                    vacancy -> {
                        String url = String
                                .join("", baseAddress,
                                        "/negotiations?vacancy_id=", vacancy.substring(vacancy.lastIndexOf("/") + 1),
                                        "&resume_id=", candidate.getResumeId(),
                                        "&enable_applicant_visibility_in_country=true");

                        restTemplate.postForEntity(url, null, String.class);
                    }
            );
        } else {
            String[] vacanciesId = count.split(",");

            Arrays.stream(vacanciesId).forEach(
                    vacancy -> {
                        String vacancyId = vacanciesOnResponse.get(Integer.parseInt(vacancy));

                        String url = String
                                .join("", baseAddress,
                                        "/negotiations?vacancy_id=", vacancyId.substring(vacancyId.lastIndexOf("/") + 1),
                                        "&resume_id=", candidate.getResumeId(),
                                        "&enable_applicant_visibility_in_country=true");

                        restTemplate.postForEntity(url, null, String.class);
                    }
            );
        }

        return state;
    }
}
