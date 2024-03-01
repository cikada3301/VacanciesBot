package com.example.vacanciesbot.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class VacancyPropertyObject {
    private String id;
    private String name;
    private String alternate_url;
}
