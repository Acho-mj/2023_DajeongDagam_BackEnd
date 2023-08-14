package com.dagagam.dagagamweb.entity;

import com.dagagam.dagagamweb.constant.DictionaryState;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
public class Dictionary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String word;
    private String description;

    @ManyToOne
    @JoinColumn(name = "participant_id")
    private Member participant; // 참여자

    @Column(name = "likes")
    private int likes;
    private LocalDateTime date;
    public Dictionary() {
        this.date = LocalDateTime.now();
    }
    @Enumerated(EnumType.STRING)
    private DictionaryState dictionaryState; // 상태(접근가능, 불가능)

    public void setDictionaryState(DictionaryState dictionaryState) {
        this.dictionaryState = dictionaryState;
    }

    public DictionaryState getDictionaryState() {
        return dictionaryState;
    }
}