package com.dagagam.dagagamweb.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DictRequestDto {
    private String word;
    private String description;
    private Long userId;
}

