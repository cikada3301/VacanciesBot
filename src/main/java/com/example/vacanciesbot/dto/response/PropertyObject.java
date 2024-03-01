package com.example.vacanciesbot.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PropertyObject {
    private List<Property> rich_text;
    private List<Property> title;
    private Property select;
    private String url;
}
