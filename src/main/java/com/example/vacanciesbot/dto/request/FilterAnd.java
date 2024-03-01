package com.example.vacanciesbot.dto.request;

import com.example.vacanciesbot.dto.request.api.IFilter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FilterAnd implements IFilter {

    private List<OperatorSelect> and;
}
