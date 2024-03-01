package com.example.vacanciesbot.service.impl;

import com.example.vacanciesbot.contracts.input.hh.CreateHHAuthCodeUseCase;
import com.example.vacanciesbot.entity.Candidate;
import com.example.vacanciesbot.repository.CandidateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
public class AuthorizeService implements CreateHHAuthCodeUseCase {

    private final CandidateRepository candidateRepository;

    @Value("${hh.clientId}")
    private String clientId;

    @Value("${hh.secret}")
    private String clientSecret;

    @Value("${web.address}")
    private String baseUrl;

    @Value("${organisationId}")
    private String organisationId;

    @Override
    public String getAuthToken(String code, String id) {
        RestTemplate restTemplate = new RestTemplate();

        String[] args = code.split(" ");

        String url = String
                .join("", baseUrl,
                        "/oauth/token?grant_type=authorization_code&code=", args[0],
                        "&client_id=", clientId,
                        "&client_secret=", clientSecret);

        Tokens secureKeys = restTemplate.postForEntity(url, null, Tokens.class).getBody();

        if (secureKeys == null) {
            return "Не client_id or client_secret not found";
        }

        Candidate candidate = candidateRepository.findById(Long.valueOf(id)).orElseThrow();

        candidate.setAccess(secureKeys.access_token);
        candidate.setRefresh(secureKeys.refresh_token);
        candidate.setExpireTime(ZonedDateTime.now().plusSeconds(secureKeys.expires_in));
        candidate.setResumeId(args[1]);

        candidateRepository.save(candidate);

        return "Auth";
    }

    @Override
    public String checkExpired(Long id) {
        RestTemplate restTemplate = new RestTemplate();

        Candidate candidate = candidateRepository.findById(id).orElseThrow();

        if (candidate.getAccess() == null) {
            return "NotAuth";
        }

        if (!ZonedDateTime.now().isBefore(candidate.getExpireTime())) {
            String url = String
                    .join("", baseUrl,
                            "/oauth/token?grant_type=refresh_token&refresh_token=", candidate.getRefresh(),
                            "&client_id=", clientId,
                            "&client_secret=", clientSecret);

            Tokens secureKeys = restTemplate.postForEntity(url, null, Tokens.class).getBody();

            if (secureKeys == null) {
                return "";
            }

            candidate.setAccess(secureKeys.access_token);
            candidate.setRefresh(secureKeys.refresh_token);
            candidate.setExpireTime(ZonedDateTime.now().plusSeconds(secureKeys.expires_in));

            candidateRepository.save(candidate);
        }

        return "Auth";
    }

    private record Tokens(String access_token, String refresh_token, Long expires_in) {
    }
}
