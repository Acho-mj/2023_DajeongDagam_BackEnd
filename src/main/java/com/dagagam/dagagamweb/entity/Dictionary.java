package com.dagagam.dagagamweb.entity;

import com.dagagam.dagagamweb.constant.DictionaryState;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

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
    @JoinColumn(name = "creator_id")
    private Member creator;

    @ManyToMany
    @JoinTable(
            name = "dictionary_participants",
            joinColumns = @JoinColumn(name = "dictionary_id"),
            inverseJoinColumns = @JoinColumn(name = "participant_id")
    )
    private List<Member> participants = new ArrayList<>();

    private int likes;
    private LocalDateTime date;

    @Enumerated(EnumType.STRING)
    private DictionaryState dictionaryState;

    public Dictionary() {
        this.date = LocalDateTime.now();
    }

    public void setDictionaryState(DictionaryState dictionaryState) {
        this.dictionaryState = dictionaryState;
    }

    public DictionaryState getDictionaryState() {
        return dictionaryState;
    }
}