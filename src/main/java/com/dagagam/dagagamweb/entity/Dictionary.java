package com.dagagam.dagagamweb.entity;

import com.dagagam.dagagamweb.constant.DictionaryState;
import com.dagagam.dagagamweb.dto.DictRequestDto;
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
    private Set<Member> participants = new HashSet<>();

    private int likes;
    private LocalDateTime date;
    private int participantsCount; // 참여자 수


    @Enumerated(EnumType.STRING)
    private DictionaryState dictionaryState;

    public Dictionary() {
        this.date = LocalDateTime.now();
    }

    public Dictionary(DictRequestDto dictRequestDto, Member member) {
        this.word = dictRequestDto.getWord();
        this.description = dictRequestDto.getDescription();
        this.creator = member;
        this.likes = 0;
        this.dictionaryState = DictionaryState.ACCESSIBLE;
        this.date = LocalDateTime.now();
    }

    public void setDictionaryState(DictionaryState dictionaryState) {
        this.dictionaryState = dictionaryState;
    }

    public DictionaryState getDictionaryState() {
        return dictionaryState;
    }

    // setParticipantsCount 메서드 추가
    public void setParticipantsCount(int participantsCount) {
        this.participantsCount = participantsCount;
    }

    // setParticipants 메서드 추가
    public void setParticipants(Set<Member> participants) {
        this.participants = participants;
    }

}