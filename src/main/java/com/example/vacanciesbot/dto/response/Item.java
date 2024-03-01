package com.example.vacanciesbot.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Item {
    private String id;
    private String alternate_url;
    private VacancyPropertyObject employer;
    private VacancyPropertyObject vacancy;
}
