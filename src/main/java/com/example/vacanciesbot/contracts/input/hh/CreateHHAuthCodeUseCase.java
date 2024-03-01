package com.example.vacanciesbot.contracts.input.hh;

public interface CreateHHAuthCodeUseCase {
    String getAuthToken(String code, String id);
    String checkExpired(Long id);
}
