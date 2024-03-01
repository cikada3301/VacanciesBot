package com.example.vacanciesbot.service.impl;

import com.example.vacanciesbot.dto.request.*;
import com.example.vacanciesbot.dto.request.api.IOperator;
import com.example.vacanciesbot.dto.response.NotionResponseDTO;
import com.example.vacanciesbot.dto.response.Page;
import com.example.vacanciesbot.entity.Candidate;
import com.example.vacanciesbot.repository.CandidateRepository;
import com.example.vacanciesbot.service.CandidateService;
import lombok.RequiredArgsConstructor;
import org.apache.http.entity.ContentType;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CandidateServiceImpl implements CandidateService {

    private final CandidateRepository candidateRepository;
    private final String token = "secret_bhPmG73SmJHyOClvh33cRPhw9jF5z2xYQtOeMaICiZR";
    private final String urlUsers = "https://api.notion.com/v1/databases/dc1f70da8928428aa1c763de746e5953/query";
    private final String notionVersion = "2022-06-28";

    @Override
    @Transactional
    public Candidate findByNameAndLastname(String name, String lastname) {
        return candidateRepository.findByNameAndLastname(name, lastname).orElseThrow(() -> new RuntimeException("Кандидат не найден"));
    }

    @Override
    @Transactional
    public void update() {

        candidateRepository.deleteAll();

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().add("Authorization", "Bearer " + token);
            request.getHeaders().add("Notion-Version", notionVersion);
            return execution.execute(request, body);
        });

        NotionRequestDTO requestDTO = new NotionRequestDTO();
        FilterOr filterOr = new FilterOr();
        List<IOperator> or = new ArrayList<>();
        or.add(new OperatorSelect("%7Ch%7D%3F", new Status("HH")));
        or.add(new OperatorSelect("%7Ch%7D%3F", new Status("Тестовые собесы")));
        or.add(new OperatorSelect("%7Ch%7D%3F", new Status("Реальные собесы")));
        or.add(new OperatorSelect("%7Ch%7D%3F", new Status("Оффер")));
        or.add(new OperatorSelect("%7Ch%7D%3F", new Status("Инструкция")));
        filterOr.setOr(or);
        requestDTO.setFilter(filterOr);

        ResponseEntity<NotionResponseDTO> responseEntity = restTemplate.postForEntity(urlUsers,
                requestDTO, NotionResponseDTO.class, ContentType.APPLICATION_JSON);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            for (Page result : responseEntity.getBody().getResults()) {
                try {
                    if (result.getProperties().get("Статус").getSelect().getName().equalsIgnoreCase("окончил обучение")
                            || result.getProperties().get("Статус").getSelect().getName().equalsIgnoreCase("скоро окончит")) {
                        String username = result.getProperties().get("ФИО").getTitle().get(0).getPlain_text();
                        String position = result.getProperties().get("Направление").getSelect().getName();
                        String[] name = username.split(" ");
                        candidateRepository.save(Candidate.builder()
                                .name(name[1])
                                .lastname(name[0])
                                .position(position)
                                .build());
                    }
                } catch (Exception e) {
                    e.getStackTrace();
                }
            }
        } else {
            throw new RuntimeException(responseEntity.getStatusCode().toString());
        }
    }
}
