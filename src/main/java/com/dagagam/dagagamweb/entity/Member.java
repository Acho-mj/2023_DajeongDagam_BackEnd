package com.dagagam.dagagamweb.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;

    @OneToMany(mappedBy = "creator")
    private List<Dictionary> createdDictionaries;   // 생성한 사전

    @ManyToMany(mappedBy = "participants")
    private List<Dictionary> participatedDictionaries;  //  참여한 사전


}