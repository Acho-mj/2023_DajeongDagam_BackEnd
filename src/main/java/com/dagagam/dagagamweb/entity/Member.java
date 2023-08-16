package com.dagagam.dagagamweb.entity;

import com.dagagam.dagagamweb.constant.MemberAuthority;
import com.dagagam.dagagamweb.dto.MemberFormDto;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.List;

@Entity
@Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    @OneToMany(mappedBy = "creator")
    private List<Dictionary> createdDictionaries;   // 생성한 사전

    @ManyToMany(mappedBy = "participants")
    private List<Dictionary> participatedDictionaries;  //  참여한 사전

    @Enumerated(EnumType.STRING)
    private MemberAuthority memberAuthority;

    public void updatePassword(PasswordEncoder passwordEncoder, String password){
        this.password = passwordEncoder.encode(password);
    }

    public boolean matchPassword(PasswordEncoder passwordEncoder, String checkPassword){
        return passwordEncoder.matches(checkPassword, getPassword());
    }

}