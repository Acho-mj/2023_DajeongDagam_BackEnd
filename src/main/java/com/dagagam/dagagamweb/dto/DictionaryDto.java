package com.dagagam.dagagamweb.dto;

import com.dagagam.dagagamweb.entity.Dictionary;
import com.dagagam.dagagamweb.entity.Member;
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
    private String creatorName; // 생성자 이름
    private int participantsCount;  // 참여자 수
}
