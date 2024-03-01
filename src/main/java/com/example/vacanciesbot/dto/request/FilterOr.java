package com.example.vacanciesbot.dto.request;

import com.example.vacanciesbot.dto.request.api.IFilter;
import com.example.vacanciesbot.dto.request.api.IOperator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FilterOr implements IFilter {

    private List<IOperator> or;
}
