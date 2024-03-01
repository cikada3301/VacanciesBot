package com.example.vacanciesbot.dto.request;

import com.example.vacanciesbot.dto.request.api.IOperator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OperatorStatus implements IOperator {

    private String property;
    private Status select;
}
