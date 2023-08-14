package com.dagagam.dagagamweb.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class DictionaryDto {
    private Long id;
    private String word;
    private String description;
    private int likes;
    private LocalDateTime date;
    private String participantName; // 참여자 이름
}
