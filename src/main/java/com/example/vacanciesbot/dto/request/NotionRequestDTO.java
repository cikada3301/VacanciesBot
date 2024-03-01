package com.example.vacanciesbot.dto.request;

import com.example.vacanciesbot.dto.request.api.IFilter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NotionRequestDTO {

    private IFilter filter;
}
